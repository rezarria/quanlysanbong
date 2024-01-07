package io.rezarria.api.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rezarria.api.action.Model;
import io.rezarria.api.system.AccountController.UpdateDTO;
import io.rezarria.dto.post.OrganizationPostDTO;
import io.rezarria.dto.update.OrganizationUpdateDTO;
import io.rezarria.mapper.OrganizationMapper;
import io.rezarria.mapper.OrganizationUpdateDTOMapper;
import io.rezarria.projection.OrganizationInfo;
import io.rezarria.service.OrganizationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.UUID;

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
    public ResponseEntity<?> getAll(@RequestParam @Nullable UUID id,
                                    @RequestParam @Nullable Integer size,
                                    @RequestParam @Nullable Integer page) {
        if (id != null)
            return ResponseEntity.ok(service.getByIdProjection(id, OrganizationInfo.class).orElseThrow());
        if (size != null && page != null) {
            return ResponseEntity.ok(service.getPage(Pageable.ofSize(size).withPage(page), OrganizationInfo.class));
        }
        return ResponseEntity.ok(service.getAll(OrganizationInfo.class));
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
        Model.update(dto.id(), dto.patch(), objectMapper, service.getRepo()::findByIdForUpdate, service.getRepo()::findById, organizationUpdateDTOMapper::patch, OrganizationUpdateDTO.class);
        return ResponseEntity.ok().build();
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
