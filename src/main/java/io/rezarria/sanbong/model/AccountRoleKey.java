package io.rezarria.sanbong.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRoleKey implements Serializable {
    @Column(name = "account_id")
    private UUID accountId;
    @Column(name = "role_id")
    private UUID roleId;
}
