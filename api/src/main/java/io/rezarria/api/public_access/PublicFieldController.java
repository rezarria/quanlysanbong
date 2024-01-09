package io.rezarria.api.public_access;


import io.rezarria.projection.FieldInfo;
import io.rezarria.service.FieldHistoryService;
import io.rezarria.service.FieldService;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/field")
public class PublicFieldController {
    private final FieldService fieldService;
    private final FieldHistoryService fieldHistoryService;

    @GetMapping
    public ResponseEntity<?> getFieldList(@RequestParam @Nullable UUID id, @RequestParam @Nullable Integer size, @RequestParam @Nullable Integer page) {
        if (id != null) {
            return ResponseEntity.ok(fieldService.findByIdProjection(id, FieldInfo.class).orElseThrow());
        }
        if (size != null && page != null) {
            var data = fieldService.getPublicPage(Pageable.ofSize(size).withPage(page), FieldInfo.class);
            return ResponseEntity.ok(data);
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(400));
    }

    @GetMapping("schedule")
    public ResponseEntity<?> getSchedule(@RequestParam UUID id) {
        var data = fieldHistoryService.getSchedule(id);
        return ResponseEntity.ok(data.stream().map(i -> {
            var builder = ScheduleDTO.builder();
            builder.id(i.getId());
            builder.from(i.getFrom());
            builder.to(i.getTo());
            return builder.build();
        }).toList());
    }

    @Builder
    public record ScheduleDTO(UUID id, Instant from, Instant to) {
    }
}