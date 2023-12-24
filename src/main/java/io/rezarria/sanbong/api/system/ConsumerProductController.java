package io.rezarria.sanbong.api.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import io.rezarria.sanbong.dto.PatchDTO;
import io.rezarria.sanbong.dto.delete.DeleteDTO;
import io.rezarria.sanbong.dto.post.ConsumerProductPost;
import io.rezarria.sanbong.dto.update.consumer_product.ConsumerProductUpdateDTO;
import io.rezarria.sanbong.dto.update.consumer_product.ConsumerProductUpdateDTOMapper;
import io.rezarria.sanbong.mapper.ConsumerProductMapper;
import io.rezarria.sanbong.service.ConsumerProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
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
@RequiredArgsConstructor
@RequestMapping(name = "/api/consumerProduct")
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
    public ResponseEntity<?> getAll(@PathVariable @RequestParam Optional<UUID> id,
                                    @RequestParam Optional<String> name) {
        if (name.isPresent()) {
            Streamable<GetDTO> data = consumerProductService.getRepo().findAllByNameContaining(name.get(),
                    GetDTO.class);
            return ResponseEntity.ok(data);
        }
        if (id.isPresent()) {
            return ResponseEntity
                    .ok(consumerProductService.getRepo().findByIdProject(id.get(), GetDTO.class).orElseThrow());
        }
        return ResponseEntity.ok(consumerProductService.getStream(GetDTO.class));
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

        var currentDTO = consumerProductService.getRepo().findByIdForUpdate(dto.getId()).orElseThrow();
        JsonNode nodePatched = dto.getPatch().apply(objectMapper.convertValue(currentDTO, JsonNode.class));
        var fieldPatched = objectMapper.treeToValue(nodePatched, ConsumerProductUpdateDTO.class);
        var field = consumerProductService.get(dto.getId());
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

}