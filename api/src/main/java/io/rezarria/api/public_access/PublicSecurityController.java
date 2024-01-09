package io.rezarria.api.public_access;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.rezarria.service.CustomerService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/api/security")
@RequiredArgsConstructor
public class PublicSecurityController {

    @Lazy
    private final CustomerService customerService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        customerService.register(dto.getName(), dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok().build();
    }

    @Data
    public static class RegisterDTO {
        private String name;
        private String email;
        private String password;
    }
}
