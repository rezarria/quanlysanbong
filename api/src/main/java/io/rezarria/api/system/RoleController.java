package io.rezarria.api.system;

import java.util.Collection;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        if (name != null) {
            return ResponseEntity.ok(roleService.findAllByName(name));
        }
        if (id != null)
            return ResponseEntity.ok(roleService.getByIdProjection(id, RoleInfo.class).orElseThrow());
        if (size != null && page != null) {
            return ResponseEntity.ok(roleService.getPage(Pageable.ofSize(size).withPage(page), RoleInfo.class));
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
