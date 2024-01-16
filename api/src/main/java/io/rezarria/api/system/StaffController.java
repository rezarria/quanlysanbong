package io.rezarria.api.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rezarria.api.action.Model;
import io.rezarria.dto.PatchDTO;
import io.rezarria.dto.delete.DeleteDTO;
import io.rezarria.dto.post.StaffPostDTO;
import io.rezarria.dto.update.StaffUpdateDTO;
import io.rezarria.mapper.StaffMapper;
import io.rezarria.mapper.StaffUpdateDTOMapper;
import io.rezarria.projection.StaffInfo;
import io.rezarria.service.StaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class StaffController {
    private final StaffService staffService;
    private final StaffMapper staffMapper;
    private final ObjectMapper objectMapper;
    private final StaffUpdateDTOMapper staffUpdateDTOMapper;

    @GetMapping(produces = "application/json")
    @Transactional
    public ResponseEntity<?> getAll(@RequestParam @Nullable UUID id, @RequestParam @Nullable Integer page,
            @RequestParam @Nullable Integer size, @RequestParam @Nullable String name) {
        if (id != null) {
            return ResponseEntity.ok(staffService.getById(id, StaffInfo.class).orElseThrow());
        }
        if (page != null && size != null) {
            var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
            if (name != null)
                return ResponseEntity.ok(staffService.getPageContainName(name, pageable, StaffInfo.class));
            return ResponseEntity.ok(staffService.getPage(pageable, StaffInfo.class));
        }
        return ResponseEntity.ok(staffService.all(StaffInfo.class));
    }

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(staffService.getSize());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody StaffPostDTO dto) {
        var staff = staffMapper.convert(dto);
        staffService.create(staff);
        return ResponseEntity.ok().build();
    }

    @GetMapping("beforeUpdate")
    public ResponseEntity<?> beforeUpdate(@RequestParam UUID id) {
        return ResponseEntity.ok(staffService.getRepo().getUpdateById(id).orElseThrow());
    }

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody PatchDTO dto) {
        staffService.update(Model.update(dto.id(), dto.patch(), objectMapper, staffService.getRepo()::getUpdateById,
                staffService.getRepo()::findById, staffUpdateDTOMapper::patch, StaffUpdateDTO.class));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody DeleteDTO dto) {
        staffService.removeIn(dto.ids());
        return ResponseEntity.ok().build();
    }

}
