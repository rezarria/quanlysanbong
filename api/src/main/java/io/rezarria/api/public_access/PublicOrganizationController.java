package io.rezarria.api.public_access;


import io.rezarria.projection.OrganizationInfo;
import io.rezarria.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/public/api/organization")
@RequiredArgsConstructor
public class PublicOrganizationController {
    private final OrganizationService service;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam UUID id) {
        return ResponseEntity.ok(service.findByIdProjection(id, OrganizationInfo.class).orElseThrow());
    }

    @GetMapping("detail")
    public ResponseEntity<?> detail(@RequestParam UUID id) {
        return ResponseEntity.ok(service.getDetailById(id).orElseThrow());
    }

    @PostMapping("ids")
    public ResponseEntity<?> getOrganizationsById(@RequestBody Set<UUID> ids) {
        var page = service.findByIds(ids, Pageable.ofSize(100).withPage(0), OrganizationInfo.class);
        return ResponseEntity.ok(page.get());
    }

    public record RequestDTO(Set<UUID> ids) {
    }
}
