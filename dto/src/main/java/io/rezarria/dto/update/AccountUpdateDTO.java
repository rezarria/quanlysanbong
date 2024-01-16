package io.rezarria.dto.update;

import io.rezarria.model.Account;
import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;
import lombok.Builder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record AccountUpdateDTO(UUID id, String username, UUID userId, UUID roleIds) {
    public static AccountUpdateDTO create(Account account) {
        var builder = AccountUpdateDTO.builder();
        builder.id(account.getId());
        builder.username(account.getUsername());
        if (account.getUser() != null)
            builder.userId(account.getUser().getId());
        if (!account.getRoles().isEmpty()) {
            builder.roleIds(account.getRoles().get(0).getId().getRoleId());
        }
        return builder.build();
    }
}
