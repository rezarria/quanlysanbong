package io.rezarria.sanbong.api.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.rezarria.sanbong.dto.LoginDTO;
import io.rezarria.sanbong.dto.RegisterDTO;
import io.rezarria.sanbong.security.service.SecurityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/security")
@SecurityRequirement(name = "bearer-jwt")
@RequiredArgsConstructor
public class SecurityController {
    private final SecurityService securityService;

    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(securityService.getAllAccount().stream().map(i -> (long) i.getRoles().size()));
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        Map<String, String> json = new HashMap<>();
        json.put("jwt", securityService.login(dto.getUsername(), dto.getPassword()));
        return ResponseEntity.ok(json);
    }

    @GetMapping(path = "/checkInfo", produces = "application/json")
    public ResponseEntity<?> check() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(securityService.register(dto.getUsername(), dto.getPassword()));
    }

}
