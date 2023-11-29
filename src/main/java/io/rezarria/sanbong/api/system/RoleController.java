package io.rezarria.sanbong.api.system;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.rezarria.sanbong.dto.PatchDTO;
import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.security.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    @Lazy
    private final RoleService roleService;
    @Lazy
    private final ObjectMapper objectMapper;

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(roleService.getSize());
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestParam("id") Optional<UUID> id,
            @RequestParam("limit") Optional<Integer> limit) throws Exception {
        if (id.isPresent())
            return ResponseEntity.ok(roleService.get(id.get()));
        if (limit.isPresent()) {
            return ResponseEntity.ok(roleService.getRepo().findAll(Pageable.ofSize(limit.get())).get());
        }
        return ResponseEntity.ok(roleService.getAll());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody CreateDTO dto) {
        return ResponseEntity.ok(roleService.add(dto.name));
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

    public record CreateDTO(String name) {
    }
}
