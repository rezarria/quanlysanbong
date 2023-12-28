package io.rezarria.api.system;

import io.rezarria.dto.post.FieldUnitSettingPostDTO;
import io.rezarria.mapper.FieldUnitSettingMapper;
import io.rezarria.model.FieldUnitSetting;
import io.rezarria.projection.FieldUnitSettingInfo;
import io.rezarria.service.FieldService;
import io.rezarria.service.FieldUnitSettingService;
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
                service.getAllProjection(FieldUnitSettingInfo.class, FieldUnitSetting.class));
    }

    @GetMapping("byFieldId")
    public ResponseEntity<FieldUnitSettingInfo> getByFieldId(@RequestParam UUID id) {
        return ResponseEntity.ok(service.getSettingFromFieldId(id, FieldUnitSettingInfo.class).orElseThrow());
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
