package io.rezarria.sanbong.api.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final ObjectMapper objectMapper;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Collection<Account>> find() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> create(@RequestBody CreateDTO dto) {
        return ResponseEntity.ok(accountService.add(Account.builder().username(dto.username).password(passwordEncoder.encode(dto.password)).build()));
    }

    @SneakyThrows
    @PatchMapping(consumes = "application/json-patch+json", produces = "application/json")
    @Transactional()
    public ResponseEntity<Account> update(@RequestParam UUID id, @RequestBody JsonPatch patch) {
        Account account = accountService.getById(id).orElseThrow();
        JsonNode nodePatched = patch.apply(objectMapper.convertValue(account, JsonNode.class));
        Account accountPatched = objectMapper.treeToValue(nodePatched, Account.class);
        accountPatched = accountService.update(accountPatched);
        return ResponseEntity.ok(accountPatched);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Collection<UUID> ids) {
        accountService.delete(ids);
        return ResponseEntity.ok().build();
    }

    public record CreateDTO(String username, String password, Collection<UUID> roles) {
    }

}
