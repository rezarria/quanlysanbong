package io.rezarria.sanbong.api.public_access;

import io.rezarria.sanbong.projection.FieldGetDTO;
import io.rezarria.sanbong.service.FieldService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/field")
public class PublicFieldController {
    private final FieldService fieldService;

    @GetMapping
    public ResponseEntity<?> getFieldList(@RequestParam @Nullable UUID id, @RequestParam @Nullable Integer size, @RequestParam @Nullable Integer page) {
        if (id != null) {
            return ResponseEntity.ok(fieldService.findByIdProjection(id, FieldGetDTO.class).orElseThrow());
        }
        if (size != null && page != null) {
            var data = fieldService.getPublicPage(Pageable.ofSize(size).withPage(page), FieldGetDTO.class);
            return ResponseEntity.ok(data);
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(400));
    }
}