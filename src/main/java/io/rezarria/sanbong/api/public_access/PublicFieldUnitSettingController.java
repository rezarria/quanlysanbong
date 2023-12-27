package io.rezarria.sanbong.api.public_access;

import io.rezarria.sanbong.projection.FieldUnitSettingGetDTO;
import io.rezarria.sanbong.service.FieldUnitSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/public/api/fieldUnitSetting")
@RequiredArgsConstructor
public class PublicFieldUnitSettingController {
    private final FieldUnitSettingService service;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam UUID id) {
        var data = service.getSettingFromFieldId(id, FieldUnitSettingGetDTO.class).orElseThrow();
        return ResponseEntity.ok(data);
    }
}
