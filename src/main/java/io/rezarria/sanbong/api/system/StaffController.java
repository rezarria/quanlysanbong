package io.rezarria.sanbong.api.system;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.rezarria.sanbong.dto.post.StaffPostDTO;
import io.rezarria.sanbong.mapper.StaffMapper;
import io.rezarria.sanbong.projection.StaffGetDTO;
import io.rezarria.sanbong.service.StaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
            return ResponseEntity.ok(service.getById(id, StaffGetDTO.class));
        }
        if (page != null && size != null) {
            return ResponseEntity.ok(service.getPage(Pageable.ofSize(size).withPage(page), StaffGetDTO.class));
        }
        return ResponseEntity.ok(service.all(StaffGetDTO.class));
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
