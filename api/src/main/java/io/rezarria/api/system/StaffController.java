package io.rezarria.api.system;

import io.rezarria.dto.post.StaffPostDTO;
import io.rezarria.mapper.StaffMapper;
import io.rezarria.projection.StaffInfo;
import io.rezarria.service.StaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class StaffController {
    private final StaffService service;
    private final StaffMapper mapper;

    @GetMapping(produces = "application/json")
    @Transactional
    public ResponseEntity<?> getAll(@RequestParam @Nullable UUID id, @RequestParam @Nullable Integer page,
                                    @RequestParam @Nullable Integer size) {
        if (id != null) {
            return ResponseEntity.ok(service.getById(id, StaffInfo.class));
        }
        if (page != null && size != null) {
            return ResponseEntity.ok(service.getPage(Pageable.ofSize(size).withPage(page), StaffInfo.class));
        }
        return ResponseEntity.ok(service.all(StaffInfo.class));
    }

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(service.getSize());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody StaffPostDTO dto) {
        var staff = mapper.convert(dto);
        service.create(staff);
        return ResponseEntity.ok().build();
    }

}
