package io.rezarria.sanbong.api.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.security.service.RoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<?> getAll(@RequestParam("id") Optional<UUID> id) throws Exception {
        if (id.isPresent())
            return ResponseEntity.ok(roleService.get(id.get()));
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
    public ResponseEntity<?> patch(@RequestBody PatchDTO data)
            throws IllegalArgumentException, JsonPatchException, JsonProcessingException {
        Role role = roleService.get(data.id);
        if (role.getLastModifiedDate().equals(data.getTime())) {
            JsonNode nodePatched = data.patch.apply(objectMapper.convertValue(role, JsonNode.class));
            role = objectMapper.treeToValue(nodePatched, Role.class);
            role = roleService.update(role);
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    @Data
    public static class PatchDTO {
        private UUID id;
        private JsonPatch patch;
        private Instant time;
    }

    public record CreateDTO(String name) {
    }
}
