package io.rezarria.api.public_access;


import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.rezarria.projection.OrganizationInfo;
import io.rezarria.service.OrganizationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/api/organization")
@RequiredArgsConstructor
public class PublicOrganizationController {
    private final OrganizationService service;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam UUID id) {
        return ResponseEntity.ok(service.findByIdProjection(id, OrganizationInfo.class).orElseThrow());
    }

    @PostMapping("ids")
    public ResponseEntity<?> getOrganizationsById(@RequestBody Set<UUID> ids) {
        var page = service.findByIds(ids, Pageable.ofSize(100).withPage(0), OrganizationInfo.class);
        return ResponseEntity.ok(page.get());
    }

    public record RequestDTO(Set<UUID> ids) {
    }
}
