package io.rezarria.service;

import io.rezarria.dto.update.AccountUpdateDTO;
import io.rezarria.model.Account;
import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;
import io.rezarria.repository.AccountRepository;
import io.rezarria.repository.RoleRepository;
import io.rezarria.service.interfaces.IService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountService extends IService<AccountRepository, Account> {
    @Lazy
    private final PasswordEncoder passwordEncoder;
    @Lazy
    private final AccountRepository accountRepository;
    @Lazy
    private final EntityManager entityManager;
    @Lazy
    private final RoleRepository roleRepository;

    public boolean changePassword(UUID id, String oldPassword, String newPassword) {
        var account = accountRepository.findById(id).orElseThrow();
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            return false;
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        return true;
    }

    public <T> Stream<T> findByUserNull(Class<T> type) {
        return accountRepository.findByUserNull(type);
    }

    public <T> Stream<T> findByName(String name, boolean skipUser, Class<T> type) {
        if (skipUser)
            return accountRepository.findByUsernameContainsAndUserNull(name, type);
        return accountRepository.findByCreatedBy_UsernameContains(name, type);
    }

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> getById(UUID id) {
        return accountRepository.findById(id);
    }

    public Account add(Account account) {
        return accountRepository.save(account);
    }

    public void delete(Collection<UUID> ids) {
        accountRepository.deleteAllById(ids);
    }

    public <T> Page<T> getPageContainName(String nam, Pageable page, Class<T> type) {
        return getRepo().getPageContain(nam, page, type);
    }

    public <T> Page<T> getPageContainNameSkipUser(String nam, Pageable page, Class<T> type) {
        return getRepo().getPageContainSkipUser(nam, page, type);
    }

    @Nullable
    public Account register(String username, String password) {
        var account = Account
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .active(true)
                .build();
        return accountRepository.save(account);
    }

    @Override
    public AccountRepository getRepo() {
        return accountRepository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Nullable
    public Account patch(AccountUpdateDTO dto) {
        var account = accountRepository.findById(dto.id()).orElse(null);
        if (account != null) {
            var accountRoles = account.getRoles();
            accountRoles.removeIf(i -> i.getId().getRoleId() != dto.roleIds());
            if (accountRoles.isEmpty() && dto.roleIds() != null) {
                var role = roleRepository.findById(dto.roleIds()).orElseThrow();
                account.getRoles().add(AccountRole.builder()
                        .account(account)
                        .role(role)
                        .id(AccountRoleKey.builder()
                                .roleId(role.getId())
                                .accountId(account.getId())
                                .build())
                        .build());
            }
        }
        return account;
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return accountRepository.findByIdProjection(id, type);
    }

}
