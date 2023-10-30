package io.rezarria.sanbong.api.system;

import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping(produces = "application/json")
    public DeferredResult<Collection<Role>> getAll() throws Exception {
        DeferredResult<Collection<Role>> deferredResult = new DeferredResult<>();
        deferredResult.setResult(roleService.getAll().toList());
        return deferredResult;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody CreateDTO dto) {
        return ResponseEntity.ok(roleService.add(dto.name));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Collection<UUID> ids) {
        roleService.deleteAll(ids);
        return ResponseEntity.ok().build();
    }

    public record CreateDTO(String name) {
    }
}
