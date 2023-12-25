package io.rezarria.sanbong.api.system;

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
@RequestMapping("/api/fieldUnitSetting")
@RequiredArgsConstructor
public class FieldUnitSettingController {
    private final FieldUnitSettingService service;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam UUID id) {
        return ResponseEntity.ok(service.getSettingFromFieldId(id, FieldUnitSettingGetDTO.class).orElseThrow());
    }

    public ResponseEntity<?> create() {
        return null;
    }
}
