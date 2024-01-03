package io.rezarria.mapper;

import io.rezarria.dto.update.AccountUpdateDTO;
import io.rezarria.model.Account;
import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;
import io.rezarria.model.Role;
import io.rezarria.repository.AccountRoleRepository;
import io.rezarria.repository.RoleRepository;
import io.rezarria.repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AccountUpdateDTOMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "user", ignore = true)
    public abstract void convert(AccountUpdateDTO src, @MappingTarget Account dis);

    public void patch(AccountUpdateDTO src, @MappingTarget Account dis) {
        convert(src, dis);
        if (dis.getUser() == null | dis.getUser().getId() != src.userId()) {
            var user = dis.getUser();
            user.setAccount(null);
            userRepository.save(user);
            dis.setUser(userRepository.findById(src.userId()).orElseThrow());
        }
        var roles = dis.getRoles();
        accountRoleRepository.deleteAll(roles.stream().filter(
                i -> !src.roleIds().contains(i.getId().getRoleId())).toList());
        roles.removeIf(i -> !src.roleIds().contains(i.getId().getRoleId()));
        var currentroleIds = dis.getRoles().stream().map(AccountRole::getId).map(AccountRoleKey::getRoleId)
                .collect(Collectors.toSet());
        var newRoleIds = src.roleIds().stream().filter(i -> !currentroleIds.contains(i))
                .map(i -> AccountRole.builder().id(AccountRoleKey.builder().roleId(i).accountId(dis.getId()).build())
                        .account(dis)
                        .role(Role.builder().id(i).build())
                        .build())
                .toList();
        roles.addAll(newRoleIds);
    }

    @Named("toRoles")
    public Set<AccountRole> toRoles(Set<UUID> ids) {
        if (roleRepository.areIdsExist(ids))
            throw new RuntimeException("id không khớp");
        return ids.stream().map(id -> AccountRole.builder().build()).collect(Collectors.toSet());
    }
}
