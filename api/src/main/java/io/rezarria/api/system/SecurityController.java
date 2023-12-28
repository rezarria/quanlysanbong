package io.rezarria.api.system;

import io.rezarria.dto.LoginDTO;
import io.rezarria.dto.RegisterDTO;
import io.rezarria.service.SecurityService;
import io.rezarria.service.SecurityService.JwtAndRefreshRecord;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(securityService.login(dto.getUsername(), dto.getPassword()));
    }

    @PostMapping(path = "/refesh", produces = "application/json")
    public ResponseEntity<JwtAndRefreshRecord> refesh(@RequestBody String token) {
        return ResponseEntity.ok(securityService.refresh(token));
    }

    @GetMapping(path = "/checkInfo", produces = "application/json")
    public ResponseEntity<?> check() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) throws Exception {
        return ResponseEntity.ok(securityService.register(dto.getUsername(), dto.getPassword()));
    }

}
