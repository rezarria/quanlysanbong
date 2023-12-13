package io.rezarria.sanbong.security.service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.rezarria.sanbong.dto.update.AccountUpdateDTO;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.AccountRoleKey;
import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.repository.AccountRepository;
import io.rezarria.sanbong.repository.RoleRepository;
import io.rezarria.sanbong.service.IService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements IService<AccountRepository, Account> {
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

    @Nullable
    public Account register(String username, String password, Stream<Role> roles) {
        try {
            return accountRepository.save(make(username, password, roles));
        } catch (Exception e) {
            return null;
        }
    }

    public Account make(String username, String password, Stream<Role> roles) {
        return make(username, password, roles.toList());
    }

    public Account make(String username, String password, Collection<Role> roles) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setRoles(roles.stream().map(AccountRole.builder()::role).map(AccountRole.AccountRoleBuilder::build)
                .collect(Collectors.toSet()));
        return account;
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
        if (account != null && account.getRoles().stream()
                .map(AccountRole::getId)
                .map(AccountRoleKey::getRoleId)
                .collect(Collectors.toSet())
                .equals(dto.roleIds())) {
            var accountRoles = account.getRoles();
            accountRoles.removeIf(i -> !dto.roleIds().contains(i.getId().getRoleId()));
            Set<UUID> accountRoleIds = accountRoles.stream().map(AccountRole::getId).map(AccountRoleKey::getRoleId)
                    .collect(Collectors.toSet());
            var newRoleIds = dto.roleIds().stream().filter(accountRoleIds::contains).toList();
            var newRoles = roleRepository.findAllById(newRoleIds).stream()
                    .map(i -> AccountRole.builder().role(i).build()).toList();
            accountRoles.addAll(newRoles);
        }
        return account;
    }

}
