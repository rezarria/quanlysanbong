package io.rezarria.api.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import io.rezarria.dto.PatchDTO;
import io.rezarria.dto.delete.DeleteDTO;
import io.rezarria.dto.post.ConsumerProductPost;
import io.rezarria.dto.update.ConsumerProductUpdateDTO;
import io.rezarria.mapper.ConsumerProductMapper;
import io.rezarria.mapper.ConsumerProductUpdateDTOMapper;
import io.rezarria.projection.ConsumerProductInfo;
import io.rezarria.service.ConsumerProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/consumerProduct")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class ConsumerProductController {
    private final ConsumerProductService consumerProductService;
    private final ConsumerProductMapper mapper;
    private final ObjectMapper objectMapper;
    private final ConsumerProductUpdateDTOMapper updateMapper;

    @GetMapping("size")
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer-jwt")})

    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(consumerProductService.getSize());
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@PathVariable @RequestParam @Nullable UUID id,
                                    @RequestParam @Nullable String name,
                                    @RequestParam @Nullable Integer size,
                                    @RequestParam @Nullable Integer page) {
        if (size != null && page != null) {
            return ResponseEntity
                    .ok(consumerProductService.getPage(Pageable.ofSize(size).withPage(page), ConsumerProductInfo.class));
        }
        if (name != null) {
            return ResponseEntity.ok(consumerProductService.getStreamByName(name, ConsumerProductInfo.class));
        }
        if (id != null) {
            return ResponseEntity
                    .ok(consumerProductService.getRepo().findByIdProject(id, ConsumerProductInfo.class).orElseThrow());
        }
        return ResponseEntity.ok(consumerProductService.getStream(ConsumerProductInfo.class));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody ConsumerProductPost dto) {
        var product = mapper.convert(dto);
        consumerProductService.create(product);
        if (product.getPrice() != null) {
            product.getPrice().setProduct(product);
            consumerProductService.update(product);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> delete(@RequestBody DeleteDTO dto) {
        consumerProductService.removeIn(dto.ids());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(consumes = "application/json-patch+json")
    @Transactional()
    public ResponseEntity<?> update(@RequestBody PatchDTO dto)
            throws IllegalArgumentException, JsonPatchException, JsonProcessingException {

        var currentDTO = consumerProductService.getRepo().findByIdForUpdate(dto.id()).orElseThrow();
        JsonNode nodePatched = dto.patch().apply(objectMapper.convertValue(currentDTO, JsonNode.class));
        var fieldPatched = objectMapper.treeToValue(nodePatched, ConsumerProductUpdateDTO.class);
        var field = consumerProductService.get(dto.id());
        updateMapper.patch(fieldPatched, field);
        consumerProductService.update(field);
        return ResponseEntity.ok(field);
    }

    @GetMapping("/beforeUpdate")
    public ResponseEntity<?> getDataBeforeUpdate(@RequestParam UUID id) {
        var data = consumerProductService.getRepo().findByIdForUpdate(id);
        if (data.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(data.get());
    }
}
