package io.rezarria.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OrderColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AccountRole extends Audit {
    @EmbeddedId
    @OrderColumn()
    @Builder.Default
    private AccountRoleKey id = new AccountRoleKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private Role role;

}
