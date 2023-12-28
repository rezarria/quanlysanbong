package io.rezarria.update;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.AccountRoleKey;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record AccountUpdateDTO(UUID id, String username, UUID userId, Set<UUID> roleIds) {
    public static AccountUpdateDTO create(Account account) {
        var builder = AccountUpdateDTO.builder();
        builder.id(account.getId());
        builder.username(account.getUsername());
        if (account.getUser() != null)
            builder.userId(account.getUser().getId());
        builder.roleIds(account.getRoles().stream().map(AccountRole::getId).map(AccountRoleKey::getRoleId)
                .collect(Collectors.toSet()));
        return builder.build();
    }
}
