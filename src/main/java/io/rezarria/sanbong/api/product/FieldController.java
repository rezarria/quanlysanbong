package io.rezarria.sanbong.api.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.rezarria.sanbong.api.product.FieldDTO.CreateDTO;
import io.rezarria.sanbong.api.product.FieldDTO.DeleteDTO;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService fieldService;
    @Qualifier("jsonPatchObjectMapper")
    private final ObjectMapper objectMapper;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(fieldService.getAll());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody CreateDTO dto) {
        fieldService.create(dto.name(), dto.picture(), dto.description());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> delete(@RequestBody DeleteDTO dto) {
        fieldService.remove(dto.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> update(@RequestParam UUID id, @RequestBody JsonPatch patch)
            throws IllegalArgumentException, JsonPatchException, JsonProcessingException {
        Field field = fieldService.get(id);
        JsonNode nodePatched = patch.apply(objectMapper.convertValue(field, JsonNode.class));
        Field fieldPatched = objectMapper.treeToValue(nodePatched, Field.class);
        fieldPatched = fieldService.update(fieldPatched);
        return ResponseEntity.ok(fieldPatched);
    }
}
