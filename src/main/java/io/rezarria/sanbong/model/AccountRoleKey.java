package io.rezarria.sanbong.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRoleKey implements Serializable {
    @Column(name = "account_id")
    private UUID accountId;
    @Column(name = "role_id")
    private UUID roleId;
}
