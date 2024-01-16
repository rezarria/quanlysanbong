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
import io.rezarria.projection.AccountInfo;
import io.rezarria.service.AccountService;
import io.rezarria.service.RoleService;
import io.rezarria.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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
    private final RoleService roleService;
    @Lazy
    private final UserService userService;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Lazy
    private final AccountUpdateDTOMapper accountUpdateDTOMapper;

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(accountService.getSize());
    }

    @GetMapping("checkUsername")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(accountService.getRepo().existsByUsername(username));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> find(@RequestParam @Nullable UUID id, @RequestParam @Nullable Integer size,
            @RequestParam @Nullable Integer page, @RequestParam @Nullable String name,
            @RequestParam @Nullable Boolean skipUser) throws NoSuchMethodException {
        if (id != null) {
            var account = accountService.getRepo().findByIdProjection(id, AccountInfo.class)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            return ResponseEntity.ok(account);
        }
        if (size != null && page != null) {
            var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
            if (name != null && !name.isBlank()) {
                return ResponseEntity.ok(accountService.getPageContainName(name, pageable, AccountInfo.class));

            }
            return ResponseEntity.ok(accountService.getRepo().findAllProjection(pageable, AccountInfo.class));
        }
        throw new NoSuchMethodException();
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

        var account = accountService.create(
                Account.builder().username(dto.username).password(passwordEncoder.encode(dto.password)).build());
        if (dto.user != null) {
            var user = userService.get(dto.user);
            user.setAccount(account);
            account.setUser(user);
        }
        if (dto.role != null) {
            var role = roleService.get(dto.role);
            account.getRoles()
                    .add(AccountRole.builder()
                            .id(AccountRoleKey.builder().roleId(role.getId()).accountId(account.getId()).build())
                            .account(account).role(role).build());
        }
        accountService.update(account);
        return ResponseEntity.ok().build();
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

        List<RoleDTO> getRoles();

        String getUsername();

        public interface RoleDTO {
            UUID getId();

            String getName();

            String getDisplayName();
        }
    }

    public record UpdateDTO(UUID id, JsonPatch patch) {
    }

    public record CreateDTO(String username, String password, @Nullable UUID user, UUID role) {
    }

    /**
     * InnerAccountController
     */
    public record UpdateModelDTO(UUID id, Optional<Set<UUID>> roleIds, Optional<String> title, Optional<UUID> userIds) {
    }

}
