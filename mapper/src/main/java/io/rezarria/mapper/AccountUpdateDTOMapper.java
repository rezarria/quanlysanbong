package io.rezarria.mapper;

import io.rezarria.dto.update.AccountUpdateDTO;
import io.rezarria.model.Account;
import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;
import io.rezarria.model.Role;
import io.rezarria.repository.AccountRoleRepository;
import io.rezarria.repository.RoleRepository;
import io.rezarria.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
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

    @Mapping(target = "user", ignore = true)
    public abstract void convert(AccountUpdateDTO src, @MappingTarget Account dis);

    public void patch(AccountUpdateDTO src, @MappingTarget Account dis) {
        convert(src, dis);
        if (dis.getUser() == null || dis.getUser().getId() != src.userId()) {
            var user = dis.getUser();
            user.setAccount(null);
            userRepository.save(user);
            dis.setUser(userRepository.findById(src.userId()).orElseThrow());
        }
        var roles = dis.getRoles();
        accountRoleRepository.deleteAll(roles.stream().filter(
                i -> src.roleIds() != i.getId().getRoleId()).toList());
        roles.removeIf(i -> src.roleIds() != i.getId().getRoleId());
        if (roles.isEmpty()) {
            var role = roleRepository.findById(src.roleIds()).orElseThrow();
            roles.add(AccountRole.builder()
                    .id(AccountRoleKey.builder()
                            .accountId(dis.getId())
                            .roleId(role.getId())
                            .build())
                    .role(role)
                    .account(dis).build());
        }
    }

    @Named("toRoles")
    public Set<AccountRole> toRoles(Set<UUID> ids) {
        if (roleRepository.existsByIdIn(ids))
            throw new RuntimeException("id không khớp");
        return ids.stream().map(id -> AccountRole.builder().build()).collect(Collectors.toSet());
    }
}
