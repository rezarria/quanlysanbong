package io.rezarria.api.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rezarria.api.action.Model;
import io.rezarria.dto.PatchDTO;
import io.rezarria.dto.delete.DeleteDTO;
import io.rezarria.dto.post.CustomerPostDTO;
import io.rezarria.dto.update.CustomerUpdateDTO;
import io.rezarria.mapper.CustomerMapper;
import io.rezarria.mapper.CustomerUpdateDTOMapper;
import io.rezarria.model.Customer;
import io.rezarria.projection.CustomerInfo;
import io.rezarria.service.CustomerService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    @Lazy
    private final CustomerService service;
    @Lazy
    private final CustomerMapper customerMapper;
    @Lazy
    private final CustomerUpdateDTOMapper customerUpdateDTOMapper;
    @Lazy
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<?> get(
            @RequestParam @Nullable String name,
            @RequestParam @Nullable Integer size,
            @RequestParam @Nullable Integer page,
            @RequestParam @Nullable UUID id

    ) {
        if (id != null) {
            return ResponseEntity.ok(service.getByIdProjection(id, CustomerInfo.class).orElseThrow());
        }
        if (size != null & page != null) {
            var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
            if (name != null)
                return ResponseEntity.ok(service.getPageContainName(name, pageable, CustomerInfo.class));
            return ResponseEntity.ok(service.getPage(pageable, CustomerInfo.class));
        }
        return ResponseEntity.ok(service.getAllStreamProjection(CustomerInfo.class));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerPostDTO dto) {
        var customer = customerMapper.convert(dto);
        service.create(customer);
        return ResponseEntity.ok().build();
    }

    @PostMapping("fastAdd")
    public ResponseEntity<?> fastAdd(@RequestBody FastAddDTO data) {
        var customer = Customer.builder().name(data.name).build();
        service.create(customer);
        return ResponseEntity.ok(new CustomerInfo() {
            @Override
            public UUID getId() {
                return customer.getId();
            }

            @Override
            public String getName() {
                return customer.getName();
            }
        });
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody DeleteDTO dto) {
        service.removeIn(dto.ids());
        return ResponseEntity.ok().build();
    }

    @GetMapping("beforeUpdate")
    public ResponseEntity<?> getUpdate(@RequestParam UUID id) {
        return ResponseEntity.ok(service.getUpdateById(id).orElseThrow());
    }

    @PatchMapping
    @SneakyThrows
    public ResponseEntity<?> patch(@RequestBody PatchDTO dto) {
        Model.update(dto.id(), dto.patch(), objectMapper, service.getRepo()::getUpdateById, service.getRepo()::findById, customerUpdateDTOMapper::patch, CustomerUpdateDTO.class);
        return ResponseEntity.ok().build();
    }

    /**
     * FastAddDTO
     */
    public record FastAddDTO(String name) {
    }
}
