package io.rezarria.sanbong.api.public_access;

import io.rezarria.sanbong.projection.FieldGetDTO;
import io.rezarria.sanbong.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/field")
public class PublicFieldController {
    private final FieldService fieldService;

    @GetMapping
    public ResponseEntity<Page<FieldGetDTO>> getFieldList(@RequestParam int size, @RequestParam int page) {
        var data = fieldService.getPublicPage(Pageable.ofSize(size).withPage(page), FieldGetDTO.class);
        return ResponseEntity.ok(data);
    }
}