package io.rezarria.api.system;

import io.rezarria.dto.delete.DeleteDTO;
import io.rezarria.model.Customer;
import io.rezarria.projection.CustomerInfo;
import io.rezarria.service.CustomerService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

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

    @PatchMapping
    public ResponseEntity<?> patch() {
        return null;
    }

    /**
     * FastAddDTO
     */
    public record FastAddDTO(String name) {
    }
}
