package io.rezarria.api.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import io.rezarria.dto.PatchDTO;
import io.rezarria.dto.delete.DeleteDTO;
import io.rezarria.dto.post.FieldPost;
import io.rezarria.dto.post.OrderPostDTO;
import io.rezarria.dto.update.FieldUpdateDTO;
import io.rezarria.mapper.FieldMapper;
import io.rezarria.mapper.FieldUpdateDTOMapper;
import io.rezarria.model.Field;
import io.rezarria.model.ProductImage;
import io.rezarria.projection.FieldHistoryInfo;
import io.rezarria.projection.FieldInfo;
import io.rezarria.service.FieldHistoryService;
import io.rezarria.service.FieldService;
import io.rezarria.service.FieldService.Status;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class FieldController {
    private final FieldService fieldService;
    private final FieldMapper fieldMapper;
    private final FieldHistoryService fieldHistoryService;
    @Qualifier("jsonPatchObjectMapper")
    private final ObjectMapper objectMapper;

    private final FieldUpdateDTOMapper fieldUpdateDTOMapper;

    @GetMapping("schedule")
    public ResponseEntity<?> test(@RequestParam UUID id) {
        return ResponseEntity.ok(fieldHistoryService.getSchedule(id, FieldHistoryInfo.class));
    }

    @GetMapping("getStatus")
    public ResponseEntity<Status> getStatus(@RequestParam UUID id) {
        return ResponseEntity.ok(fieldService.getStatus(id));
    }

    @GetMapping("isFree")
    public ResponseEntity<?> isFree(@RequestParam UUID id) {
        return ResponseEntity.ok(fieldHistoryService.isFree(id));
    }

    @GetMapping("size")
    @SecurityRequirements(value = { @SecurityRequirement(name = "bearer-jwt") })

    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(fieldService.getSize());
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@PathVariable @RequestParam @Nullable UUID id,
            @RequestParam @Nullable String name,
            @RequestParam @Nullable Integer size,
            @RequestParam @Nullable Integer page) {
        if (name != null) {
            return ResponseEntity.ok(fieldService.getRepo().findAllByNameContaining(name, FieldInfo.class).stream());
        }
        if (id != null) {
            return ResponseEntity.ok(fieldService.getRepo().findByIdProject(id, FieldInfo.class).orElseThrow());
        }
        if (size != null && page != null) {
            return ResponseEntity.ok(fieldService.getPage(Pageable.ofSize(size).withPage(page), FieldInfo.class));
        }
        var data = fieldService.getStream(FieldInfo.class);
        return ResponseEntity.ok(data);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody FieldPost dto) {
        var field = fieldMapper.convert(dto);
        fieldService.create(field);
        if (field.getPrice() != null) {
            field.getPrice().setProduct(field);
        }
        var images = dto.getImages();
        if (images != null && !images.isEmpty()) {
            field.getImages().addAll(
                    images.stream().map(img -> ProductImage.builder().product(field).path(img).build()).toList());
        }
        fieldService.update(field);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> delete(@RequestBody DeleteDTO dto) {
        fieldService.removeIn(dto.ids());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(consumes = "application/json-patch+json")
    @Transactional()
    public ResponseEntity<Field> update(@RequestBody PatchDTO dto)
            throws IllegalArgumentException, JsonPatchException, JsonProcessingException {

        var currentDTO = fieldService.getRepo().findByIdForUpdate(dto.id()).orElseThrow();
        JsonNode nodePatched = dto.patch().apply(objectMapper.convertValue(currentDTO, JsonNode.class));
        var fieldPatched = objectMapper.treeToValue(nodePatched, FieldUpdateDTO.class);
        Field field = fieldService.get(dto.id());
        fieldUpdateDTOMapper.patch(fieldPatched, field);
        fieldService.update(field);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/beforeUpdate")
    public ResponseEntity<?> getDataBeforeUpdate(@RequestParam UUID id) {
        var data = fieldService.getRepo().findByIdForUpdate(id);
        if (data.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(data.get());
    }

    @PostMapping("order")
    public ResponseEntity<?> order(@RequestBody OrderPostDTO dto) {

        return ResponseEntity.ok().build();
    }

}
