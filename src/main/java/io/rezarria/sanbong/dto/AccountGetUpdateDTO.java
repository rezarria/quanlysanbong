package io.rezarria.sanbong.dto;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class AccountGetUpdateDTO {
    private UUID id;
    @Value("#{target.user != null ? target.user.id : null}")
    private Optional<UUID> userId;
    @Value("#{target.roles.![id.roleId]}")
    private Set<UUID> roleIds;
}
