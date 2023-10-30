package io.rezarria.sanbong.api.system;

import io.rezarria.sanbong.api.system.securityDTO.LoginDTO;
import io.rezarria.sanbong.api.system.securityDTO.RegisterDTO;
import io.rezarria.sanbong.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
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
