package io.rezarria.sanbong.api.system;

import io.rezarria.sanbong.dto.post.FieldUnitSettingPostDTO;
import io.rezarria.sanbong.mapper.FieldUnitSettingMapper;
import io.rezarria.sanbong.projection.FieldUnitSettingGetDTO;
import io.rezarria.sanbong.repository.FieldUnitSettingRepository;
import io.rezarria.sanbong.service.FieldService;
import io.rezarria.sanbong.service.FieldUnitSettingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/fieldUnitSetting")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class FieldUnitSettingController {
    private final FieldUnitSettingService service;
    private final FieldUnitSettingMapper mapper;
    private final FieldService fieldService;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam UUID id) {
        return ResponseEntity.ok(
                service.getAllProjection(FieldUnitSettingGetDTO.class, FieldUnitSettingRepository.class));
    }

    @GetMapping("byFieldId")
    public ResponseEntity<FieldUnitSettingGetDTO> getByFieldId(@RequestParam UUID id) {
        return ResponseEntity.ok(service.getSettingFromFieldId(id, FieldUnitSettingGetDTO.class).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FieldUnitSettingPostDTO dto) {
        var setting = mapper.convert(dto);
        service.create(setting);
        var field = setting.getField();
        field.setCurrentUnitSetting(setting);
        fieldService.update(field);
        return ResponseEntity.ok().build();
    }
}
