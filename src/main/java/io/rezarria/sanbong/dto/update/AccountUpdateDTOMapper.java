package io.rezarria.sanbong.dto.update;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.AccountRoleKey;
import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.model.User;
import io.rezarria.sanbong.repository.RoleRepository;

@Mapper(componentModel = "spring")
public abstract class AccountUpdateDTOMapper {

    @Autowired
    private RoleRepository roleRepository;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "toUser")
    public abstract void convert(AccountUpdateDTO src, @MappingTarget Account dis);

    public void patch(AccountUpdateDTO src, @MappingTarget Account dis) {
        convert(src, dis);
        var roles = dis.getRoles();
        roles.removeIf(i -> !src.roleIds().contains(i.getId().getRoleId()));
        var currentroleIds = dis.getRoles().stream().map(AccountRole::getId).map(AccountRoleKey::getRoleId)
                .collect(Collectors.toSet());
        var newRoleIds = src.roleIds().stream().filter(i -> !currentroleIds.contains(i));
        roles.addAll(
                newRoleIds.map(i -> AccountRole.builder().role(
                        Role.builder().id(i).build()).build()).toList());
    }

    @Named("toUser")
    public User toUser(UUID id) {
        return User.builder().id(id).build();
    }

    @Named("toRoles")
    public Set<AccountRole> toRoles(Set<UUID> ids) {
        if (roleRepository.areIdsExist(ids))
            throw new RuntimeException("id không khớp");
        return ids.stream().map(id -> AccountRole.builder().build()).collect(Collectors.toSet());
    }
}
