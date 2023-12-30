package io.rezarria.mapper;

import io.micrometer.common.lang.Nullable;
import io.rezarria.dto.post.StaffPostDTO;
import io.rezarria.model.Account;
import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;
import io.rezarria.model.Staff;
import io.rezarria.repository.AccountRepository;
import io.rezarria.repository.RoleRepository;
import io.rezarria.service.AccountService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class StaffMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    public abstract Staff convert(StaffPostDTO dto);

    public Account convert(@Nullable UUID id) {
        if (id == null)
            return null;
        var r = accountRepository.findById(id);
        return r.orElse(null);
    }

    @AfterMapping
    public void postMapping(StaffPostDTO dto, @MappingTarget Staff staff) {
        if (dto.getUsername() != null) {
            Account account = Account.builder().username(dto.getUsername()).build();
            accountService.create(account);
            var roles = roleRepository.findAllById(dto.getRoles()).stream()
                    .map(i -> AccountRole.builder().role(i).id(AccountRoleKey.builder().roleId(i.getId()).build())
                            .build())
                    .toList();
            roles.forEach(i -> {
                i.setAccount(account);
                i.getId().setAccountId(account.getId());
            });
            account.getRoles().addAll(roles);
            accountService.update(account);
        }
    }
}
