package io.rezarria.sanbong.api;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.AccountRoleKey;
import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.repository.AccountRepository;
import io.rezarria.sanbong.repository.AccountRoleRepository;
import io.rezarria.sanbong.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final EntityManager entityManager;

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @GetMapping("/all2")
    public ResponseEntity<?> all2() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("/all3")
    public ResponseEntity<?> all3() {
        return ResponseEntity.ok(accountRoleRepository.findAll());
    }

    @GetMapping("/run")
    public void get() {
        accountRepository.deleteAll();
        roleRepository.deleteAll();
        accountRoleRepository.deleteAll();

        Account account = Account.builder().username("nam").build();
        Role role = Role.builder().name("admin").build();
        Role role2 = Role.builder().name("user").build();

        accountRepository.save(account);
        roleRepository.save(role);
        roleRepository.save(role2);

        entityManager.detach(role);
        entityManager.detach(role2);

        account.getRoles().add(AccountRole.builder().role(role).account(account).id(AccountRoleKey.builder().accountId(account.getId()).roleId(role.getId()).build()).build());
        account.getRoles().add(AccountRole.builder().role(role2).account(account).id(AccountRoleKey.builder().accountId(account.getId()).roleId(role2.getId()).build()).build());

        accountRepository.save(account);
    }

    @GetMapping("/run2")
    @Transactional
    public void get2() {
        accountRepository.deleteAll();
        accountRoleRepository.deleteAll();

        Account account = Account.builder().username("nam").build();
        List<UUID> list = roleRepository.find().map(RoleRepository.IdOnly::id).toList();

        accountRepository.save(account);

        list.forEach(id -> {
            Role role = Role.builder().id(id).build();
            account.getRoles().add(AccountRole.builder().role(role).account(account).id(AccountRoleKey.builder().accountId(account.getId()).roleId(role.getId()).build()).build());
        });


        accountRepository.save(account);
    }

    @GetMapping("/run3")
    @Transactional
    public void get3() {
        accountRoleRepository.deleteAll();
        accountRepository.deleteAll();
        roleRepository.deleteAll();

        Account account = Account.builder().username("nam").build();
        account.getRoles().add(
                AccountRole.builder()
                        .role(
                                Role.builder()
                                        .name("admin")
                                        .build()
                        )
                        .build()
        );
        accountRepository.save(account);
    }

    @GetMapping("/run4")
    public void get4() {
        accountRoleRepository.deleteAll();
        roleRepository.deleteAll();
        accountRepository.deleteAll();

        AccountRole accountRole = AccountRole.builder()
                .account(
                        Account.builder().username("nam").build()
                )
                .role(
                        Role.builder().name("admin").build()
                )
                .build();

        accountRoleRepository.save(accountRole);
    }

    @GetMapping("/run5")
    @Transactional
    public void get5() {
        accountRoleRepository.deleteAll();
        accountRepository.deleteAll();
        roleRepository.deleteAll();

        Account account = Account.builder().username("nam").build();
        Role role = Role.builder().name("admin").build();

        account = accountRepository.save(account);
        role = roleRepository.save(role);

        AccountRole accountRole = AccountRole.builder()
                .id(
                        AccountRoleKey.builder()
                                .accountId(account.getId())
                                .roleId(role.getId())
                                .build()
                )
                .account(
                        account
                )
                .role(
                        role
                )
                .build();

        accountRoleRepository.save(accountRole);
    }

    @GetMapping("/run6")
    @Transactional
    public void get6() {
        accountRoleRepository.deleteAll();
        accountRepository.deleteAll();

        Account account = Account.builder().username("nam").build();
        List<UUID> list = roleRepository.find().map(RoleRepository.IdOnly::id).toList();

        list.forEach(id -> {
            Role role = Role.builder().id(id).build();
            account.getRoles().add(AccountRole.builder().role(role).account(account).id(AccountRoleKey.builder().accountId(account.getId()).roleId(role.getId()).build()).build());
        });


        accountRepository.save(account);
    }

}
