package io.rezarria.api.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import io.rezarria.dto.ChangePasswordDTO;
import io.rezarria.dto.delete.DeleteDTO;
import io.rezarria.dto.update.AccountUpdateDTO;
import io.rezarria.mapper.AccountUpdateDTOMapper;
import io.rezarria.model.Account;
import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;
import io.rezarria.model.User;
import io.rezarria.service.AccountService;
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
    public ResponseEntity<?> find(@RequestParam @Nullable UUID id,
                                  @RequestParam @Nullable Integer size,
                                  @RequestParam @Nullable Integer page,
                                  @RequestParam @Nullable String name,
                                  @RequestParam @Nullable Boolean skipUser) {
        if (name != null && !name.isBlank()) {
            if (skipUser == null)
                return ResponseEntity.ok(accountService.findByName(name, false, GetDTO.class));
            else
                return ResponseEntity.ok(accountService.findByName(name, true, GetDTO.class));
        }
        if (id != null) {
            var account = accountService.getRepo().findByIdProjection(id, GetDTO.class)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            return ResponseEntity.ok(account);
        }
        if (size != null && page != null) {
            return ResponseEntity
                    .ok(accountService.getRepo().findAllProjection(Pageable.ofSize(size).withPage(page), GetDTO.class));
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
        account.getRoles().addAll(roles);
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
    public ResponseEntity<Account> update(@RequestBody UpdateDTO dto) {
        var account = accountService.getRepo().findByIdForUpdate(dto.id).orElseThrow();
        JsonNode nodePatched = dto.patch.apply(objectMapper.convertValue(account, JsonNode.class));
        var accountPatchedDTO = objectMapper.treeToValue(nodePatched, AccountUpdateDTO.class);
        var accountPatched = accountService.getRepo().findById(dto.id).orElseThrow();
        accountUpdateDTOMapper.patch(accountPatchedDTO, accountPatched);
        accountService.update(accountPatched);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody DeleteDTO dto) {
        accountService.delete(dto.ids());
        return ResponseEntity.ok().build();
    }

    public interface GetDTO {
        UUID getId();

        @Value("#{target.user != null ? target.user.id : null}")
        String getUserId();

        @Value("#{target.roles.![id.roleId]}")
        List<UUID> getRoleIds();

        String getUsername();
    }

    public record UpdateDTO(UUID id, JsonPatch patch) {
    }

    public record CreateDTO(String username, String password, @Nullable UUID user, Collection<UUID> roles) {
    }

    /**
     * InnerAccountController
     */
    public record UpdateModelDTO(UUID id, Optional<Set<UUID>> roleIds, Optional<String> title, Optional<UUID> userIds) {
    }

}
