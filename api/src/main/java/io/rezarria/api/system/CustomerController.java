package io.rezarria.system;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.rezarria.model.Customer;
import io.rezarria.projection.CustomerInfo;
import io.rezarria.service.CustomerService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

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

        }
        if (name != null)
            return ResponseEntity.ok(service.findAllByName(name, CustomerInfo.class));
        return ResponseEntity.ok(service.getAllStreamProjection(CustomerInfo.class));
    }

    /**
     * FastAddDTO
     */
    public record FastAddDTO(String name) {
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
}
