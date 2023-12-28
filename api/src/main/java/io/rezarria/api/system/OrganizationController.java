package io.rezarria.api.system;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.rezarria.dto.post.OrganizationPostDTO;
import io.rezarria.dto.update.OrganizationUpdateDTO;
import io.rezarria.mapper.OrganizationMapper;
import io.rezarria.mapper.OrganizationUpdateDTOMapper;
import io.rezarria.service.OrganizationService;
import io.rezarria.api.system.AccountController.UpdateDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class OrganizationController {
    @Lazy
    private final OrganizationService service;
    @Lazy
    private final OrganizationMapper mapper;
    @Lazy
    private final ObjectMapper objectMapper;
    @Lazy
    private final OrganizationUpdateDTOMapper organizationUpdateDTOMapper;

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(service.getSize());
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestParam Optional<UUID> id,
            @RequestParam @Nullable Integer size,
            @RequestParam @Nullable Integer page) {
        if (id.isPresent())
            return ResponseEntity.ok(service.get(id.get()));
        if (size != null && page != null) {
            return ResponseEntity.ok(service.getRepo().findAll(Pageable.ofSize(size).withPage(page)));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody OrganizationPostDTO dto) {
        return ResponseEntity.ok(service.create(mapper.convert(dto)));
    }

    @GetMapping("/beforeUpdate")
    public ResponseEntity<?> getDataBeforeUpdate(@RequestParam UUID id) {
        var data = service.getRepo().findByIdForUpdate(id);
        if (data.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(data.get());
    }

    @SneakyThrows
    @PatchMapping(consumes = "application/json-patch+json", produces = "application/json")
    @Transactional()
    public ResponseEntity<?> update(@RequestBody UpdateDTO dto) {
        var organization = service.getRepo().findByIdForUpdate(dto.id()).orElseThrow();
        JsonNode nodePatched = dto.patch().apply(objectMapper.convertValue(organization, JsonNode.class));
        var organizationPatchedDTO = objectMapper.treeToValue(nodePatched, OrganizationUpdateDTO.class);
        var organizationPatched = service.get(dto.id());
        organizationUpdateDTOMapper.patch(organizationPatchedDTO, organizationPatched);
        organizationPatched = service.update(organizationPatched);
        return ResponseEntity.ok(organizationPatched);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Collection<UUID> ids) {
        service.removeIn(ids);
        return ResponseEntity.ok().build();
    }

    interface GetDTO {
        UUID getId();

        String getName();

        String getAddress();

        String getPhone();

        String getImage();
    }
}
