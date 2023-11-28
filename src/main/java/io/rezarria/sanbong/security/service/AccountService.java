package io.rezarria.sanbong.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.repository.AccountRepository;
import io.rezarria.sanbong.service.IService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements IService {
    @Lazy
    private final PasswordEncoder passwordEncoder;
    @Lazy
    private final AccountRepository accountRepository;
    @Lazy
    private final EntityManager entityManager;

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> getById(UUID id) {
        return accountRepository.findById(id);
    }

    public Account add(Account account) {
        return accountRepository.save(account);
    }

    public Account update(Account account) {
        return entityManager.merge(account);
    }

    public void delete(Collection<UUID> ids) {
        accountRepository.deleteAllById(ids);
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
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
    public JpaRepository getRepo() {
      return accountRepository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
