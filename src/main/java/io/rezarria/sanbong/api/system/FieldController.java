package io.rezarria.sanbong.api.system;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import io.rezarria.sanbong.dto.DeleteDTO;
import io.rezarria.sanbong.dto.FieldPost;
import io.rezarria.sanbong.mapper.FieldMapper;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.repository.FieldRepository;
import io.rezarria.sanbong.service.FieldService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService fieldService;
    private final FieldMapper fieldMapper;
    @Qualifier("jsonPatchObjectMapper")
    private final ObjectMapper objectMapper;

    interface GetDTO {
        UUID getId();

        String getName();

    }

    @GetMapping(produces = "application/json", name = "/{id}")
    public ResponseEntity<?> getAll(@PathVariable @RequestParam Optional<UUID> id,
            @RequestParam Optional<String> name) {
        if (name.isPresent()) {
            Stream<GetDTO> data = ((FieldRepository) fieldService.getRepo()).findAllByNameContaining(name.get(),
                    GetDTO.class);
            return ResponseEntity.ok(data);
        }
        if (id.isPresent()) {
            return ResponseEntity.ok(fieldService.get(id.get()));
        }
        Stream<GetDTO> data = ((FieldRepository) fieldService.getRepo()).findAllStream(GetDTO.class);
        return ResponseEntity.ok(data);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody FieldPost dto) {
        var field = fieldMapper.fieldDTOtoField(dto);
        fieldService.create(field);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> delete(@RequestBody DeleteDTO dto) {
        fieldService.removeIn(dto.id());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<Field> update(@RequestParam UUID id, @RequestBody JsonPatch patch)
            throws IllegalArgumentException, JsonPatchException, JsonProcessingException {
        Field field = fieldService.get(id);
        JsonNode nodePatched = patch.apply(objectMapper.convertValue(field, JsonNode.class));
        Field fieldPatched = objectMapper.treeToValue(nodePatched, Field.class);
        fieldPatched = fieldService.update(fieldPatched);
        return ResponseEntity.ok(fieldPatched);
    }

}
