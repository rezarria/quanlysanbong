package io.rezarria.sanbong.api.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import io.rezarria.sanbong.dto.ChangePasswordDTO;
import io.rezarria.sanbong.dto.update.account.AccountUpdateDTO;
import io.rezarria.sanbong.dto.update.account.AccountUpdateDTOMapper;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.AccountRoleKey;
import io.rezarria.sanbong.model.User;
import io.rezarria.sanbong.security.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@SecurityRequirement(name = "bearer-jwt")
public class AccountController {
    @Lazy
    private final ObjectMapper objectMapper;
    @Lazy
    private final AccountService accountService;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Lazy
    private final AccountUpdateDTOMapper accountUpdateDTOMapper;

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(accountService.getSize());
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> find(@RequestParam Optional<UUID> id,
                                  @RequestParam Optional<Integer> limit,
                                  @RequestParam @Nullable String name,
                                  @RequestParam @Nullable Boolean skipUser) {
        if (name != null && !name.isBlank()) {
            if (skipUser == null)
                return ResponseEntity.ok(accountService.findByName(name, false, GetDTO.class));
            else
                return ResponseEntity.ok(accountService.findByName(name, true, GetDTO.class));
        }
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
        if (skipUser != null && skipUser) {
            return ResponseEntity.ok(accountService.findByUserNull(GetDTO.class));
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
        if (dto.user != null) {
            account.setUser(User.builder().id(dto.user).build());
        }
        account.setRoles(roles);
        return ResponseEntity.ok(accountService.update(account));
    }

    @GetMapping("/beforeUpdate")
    public ResponseEntity<?> getDataBeforeUpdate(@RequestParam UUID id) {
        var data = accountService.getRepo().findByIdForUpdate(id);
        if (data.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(data.get());
    }

    @SneakyThrows
    @PatchMapping(consumes = "application/json-patch+json", produces = "application/json")
    @Transactional()
    public ResponseEntity<Account> update(@RequestBody UpdateDTO dto) {
        var account = accountService.getRepo().findByIdForUpdate(dto.id).orElseThrow();
        JsonNode nodePatched = dto.patch.apply(objectMapper.convertValue(account, JsonNode.class));
        var accountPatchedDTO = objectMapper.treeToValue(nodePatched, AccountUpdateDTO.class);
        var accountPatched = accountService.get(dto.id);
        accountUpdateDTOMapper.patch(accountPatchedDTO, accountPatched);
        accountPatched = accountService.update(accountPatched);
        return ResponseEntity.ok(accountPatched);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Collection<UUID> ids) {
        accountService.delete(ids);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> update(@RequestBody UpdateModelDTO dto) {
        var account = accountService.getById(dto.id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok().build();
    }

    interface GetDTO {
        UUID getId();

        @Value("#{target.user != null ? target.user.id : null}")
        String getUserId();

        @Value("#{target.roles.![id.roleId]}")
        List<UUID> getRoleIds();

        String getUsername();
    }

    record UpdateDTO(UUID id, JsonPatch patch) {
    }

    public record CreateDTO(String username, String password, @Nullable UUID user, Collection<UUID> roles) {
    }

    /**
     * InnerAccountController
     */
    public record UpdateModelDTO(UUID id, Optional<Set<UUID>> roleIds, Optional<String> title, Optional<UUID> userIds) {
    }

}
