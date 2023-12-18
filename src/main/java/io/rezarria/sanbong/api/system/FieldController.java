package io.rezarria.sanbong.api.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;

import io.rezarria.sanbong.dto.PatchDTO;
import io.rezarria.sanbong.dto.delete.DeleteDTO;
import io.rezarria.sanbong.dto.post.FieldPost;
import io.rezarria.sanbong.dto.update.field.FieldUpdateDTO;
import io.rezarria.sanbong.dto.update.field.FieldUpdateDTOMapper;
import io.rezarria.sanbong.mapper.FieldMapper;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.repository.FieldRepository;
import io.rezarria.sanbong.service.FieldService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class FieldController {
    private final FieldService fieldService;
    private final FieldMapper fieldMapper;
    @Qualifier("jsonPatchObjectMapper")
    private final ObjectMapper objectMapper;

    private final FieldUpdateDTOMapper fieldUpdateDTOMapper;

    interface GetDTO {
        UUID getId();

        String getName();

        @Value("#{target.images.![path]}")
        List<String> getPictures();

        @Value("#{target.prices != null ? target.prices.![price] : null}")
        List<Double> getPrices();

        String getDescription();

        @Value("#{target.price != null ? target.price.price : null}")
        Double getPrice();
    }

    @GetMapping("size")
    @SecurityRequirements(value = { @SecurityRequirement(name = "bearer-jwt") })

    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(fieldService.getSize());
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@PathVariable @RequestParam Optional<UUID> id,
            @RequestParam Optional<String> name) {
        if (name.isPresent()) {
            Streamable<GetDTO> data = ((FieldRepository) fieldService.getRepo()).findAllByNameContaining(name.get(),
                    GetDTO.class);
            return ResponseEntity.ok(data);
        }
        if (id.isPresent()) {
            return ResponseEntity
                    .ok(fieldService.getRepo().findByIdProject(id.get(), GetDTO.class).orElseThrow());
        }
        Streamable<GetDTO> data = fieldService.getRepo().findAllStream(GetDTO.class);
        return ResponseEntity.ok(data.stream().toList());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody FieldPost dto) {
        var field = fieldMapper.fieldDTOtoField(dto);
        fieldService.create(field);
        if (field.getPrice() != null) {
            field.getPrice().setProduct(field);
            fieldService.update(field);
        }
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

        var currentDTO = fieldService.getRepo().findByIdForUpdate(dto.getId()).orElseThrow();
        JsonNode nodePatched = dto.getPatch().apply(objectMapper.convertValue(currentDTO, JsonNode.class));
        var fieldPatched = objectMapper.treeToValue(nodePatched, FieldUpdateDTO.class);
        Field field = fieldService.get(dto.getId());
        fieldUpdateDTOMapper.patch(fieldPatched, field);
        fieldService.update(field);
        return ResponseEntity.ok(field);
    }

    @GetMapping("/beforeUpdate")
    public ResponseEntity<?> getDataBeforeUpdate(@RequestParam UUID id) {
        var data = fieldService.getRepo().findByIdForUpdate(id);
        if (data.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(data.get());
    }
}
