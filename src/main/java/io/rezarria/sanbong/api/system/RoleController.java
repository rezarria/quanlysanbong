package io.rezarria.sanbong.api.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rezarria.sanbong.dto.PatchDTO;
import io.rezarria.sanbong.dto.post.RolePostDTO;
import io.rezarria.sanbong.mapper.RoleMapper;
import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.security.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    public ResponseEntity<?> getAll(@RequestParam Optional<UUID> id, @RequestParam Optional<String> name,
                                    @RequestParam @Nullable Integer size, @RequestParam @Nullable Integer page) {
        if (name.isPresent()) {
            Stream<GetDTO> data = roleService.findAllByName(name.get());
            return ResponseEntity.ok(data);
        }
        if (id.isPresent())
            return ResponseEntity.ok(roleService.get(id.get()));
        if (size != null && page != null) {
            return ResponseEntity.ok(roleService.getRepo().findAll(Pageable.ofSize(size).withPage(page)));
        }
        return ResponseEntity.ok(roleService.getAll());
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
    public ResponseEntity<?> patch(@RequestBody PatchDTO data) {
        Role role = roleService.get(data.getId());
        if (role.getLastModifiedDate().equals(data.getTime())) {
            JsonNode nodePatched = data.getPatch().apply(objectMapper.convertValue(role, JsonNode.class));
            role = objectMapper.treeToValue(nodePatched, Role.class);
            role = roleService.update(role);
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    public interface GetDTO {
        UUID id();

        String name();

        String displayName();
    }

    public record CreateDTO(String name) {
    }
}
