package io.rezarria.sanbong.api.system;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;

import io.rezarria.sanbong.dto.ChangePasswordDTO;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.AccountRoleKey;
import io.rezarria.sanbong.repository.AccountRepository;
import io.rezarria.sanbong.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    @Lazy
    private final ObjectMapper objectMapper;
    @Lazy
    private final AccountService accountService;
    @Lazy
    private final PasswordEncoder passwordEncoder;
    @Lazy
    private final AccountRepository accountRepository;

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(accountService.getSize());
    }

    interface GetDTO {
        UUID getId();

        @Value("#{target.user.id}")
        String getUserId();

        @Value("#{target.roles.![id.roleId]}")
        List<UUID> getRoleIds();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> find(@RequestParam("id") Optional<UUID> id,
            @RequestParam("limit") Optional<Integer> limit) {
        if (id.isPresent()) {
            var account = accountService.getRepo().findByIdProjection(id.get(), GetDTO.class)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            return ResponseEntity.ok(account);
        }
        if (limit.isPresent()) {
            return ResponseEntity
                    .ok(accountService.getRepo().findAllProjection(Pageable.ofSize(limit.get()), GetDTO.class)
                            .stream());
        }
        Streamable<GetDTO> data = accountService.getRepo().findAllByUsernameContaining("",
                GetDTO.class);
        return ResponseEntity.ok(data.stream());
    }

    @PostMapping(consumes = "application/json")
    @RequestMapping("changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        try {
            if (accountService.changePassword(dto.id(), dto.oldPassword(), dto.newPassword()))
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> create(@RequestBody CreateDTO dto) {

        var account = accountService
                .create(Account.builder().username(dto.username)
                        .password(passwordEncoder.encode(dto.password))
                        .build());
        Set<AccountRole> roles = dto.roles.stream()
                .map(i -> AccountRole.builder()
                        .id(AccountRoleKey.builder().roleId(i).accountId(account.getId()).build()).build())
                .collect(Collectors.toSet());
        account.setRoles(roles);
        return ResponseEntity.ok(accountService.update(account));
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

    /**
     * InnerAccountController
     */
    public record UpdateModelDTO(UUID id, Optional<Set<UUID>> roleIds, Optional<String> title, Optional<UUID> userIds) {
    }

    public ResponseEntity<?> update(@RequestBody UpdateModelDTO dto) {
        var account = accountService.getById(dto.id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok().build();
    }

}
