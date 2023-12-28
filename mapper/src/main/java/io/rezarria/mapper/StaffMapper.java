package io.rezarria.sanbong.mapper;

import io.micrometer.common.lang.Nullable;
import io.rezarria.sanbong.dto.post.StaffPostDTO;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.AccountRoleKey;
import io.rezarria.sanbong.model.Staff;
import io.rezarria.sanbong.repository.AccountRepository;
import io.rezarria.sanbong.repository.RoleRepository;
import io.rezarria.sanbong.security.service.AccountService;
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
        if (r.isEmpty())
            return null;
        return r.get();
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
