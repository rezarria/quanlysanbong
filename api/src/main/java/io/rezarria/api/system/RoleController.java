package io.rezarria.api.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rezarria.api.action.Model;
import io.rezarria.dto.PatchDTO;
import io.rezarria.dto.post.RolePostDTO;
import io.rezarria.dto.update.RoleUpdateDTO;
import io.rezarria.mapper.RoleMapper;
import io.rezarria.mapper.RoleUpdateDTOMapper;
import io.rezarria.model.Role;
import io.rezarria.projection.RoleInfo;
import io.rezarria.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class RoleController {
    @Lazy
    private final RoleService roleService;
    @Lazy
    private final ObjectMapper objectMapper;
    @Lazy
    private final RoleMapper roleMapper;
    @Lazy
    private final RoleUpdateDTOMapper roleUpdateDTOMapper;

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(roleService.getSize());
    }

    @GetMapping("getName")
    public ResponseEntity<?> getName(@RequestParam Collection<UUID> id) {
        if (id.isEmpty())
            return ResponseEntity.ok(new UUID[0]);
        var data = roleService.getMany(id).stream().map(Role::getDisplayName).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping(produces = "application/json")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAll(@RequestParam @Nullable UUID id, @RequestParam @Nullable String name, @RequestParam @Nullable Integer size, @RequestParam @Nullable Integer page) {
        if (id != null)
            return ResponseEntity.ok(roleService.getByIdProjection(id, RoleInfo.class).orElseThrow());
        if (size != null && page != null) {
            var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
            if (name != null) {
                return ResponseEntity.ok(roleService.getRepo().findByNameContains(name, pageable, RoleInfo.class));
            }
            return ResponseEntity.ok(roleService.getPage(pageable, RoleInfo.class));
        }
        return ResponseEntity.ok(roleService.getRepo().getStream(RoleInfo.class));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody RolePostDTO dto) {
        return ResponseEntity.ok(roleService.create(roleMapper.rolePostDTOToRole(dto)));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Collection<UUID> ids) {
        roleService.removeIn(ids);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    @Transactional
    @SneakyThrows
    public ResponseEntity<?> patch(@RequestBody PatchDTO dto) {
        Model.update(dto.id(), dto.patch(), objectMapper, roleService.getRepo()::createUpdateById, roleService.getRepo()::findById, roleUpdateDTOMapper::patch, RoleUpdateDTO.class);
        return ResponseEntity.ok().build();
    }

    @GetMapping("beforeUpdate")
    public ResponseEntity<?> beforeUpdate(@RequestParam UUID id) {
        return ResponseEntity.ok(roleService.getRepo().createUpdateById(id).orElseThrow());
    }

}
