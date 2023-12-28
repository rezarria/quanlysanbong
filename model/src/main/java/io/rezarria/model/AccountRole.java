package io.rezarria.model;

import jakarta.persistence.CascadeType;
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
    private AccountRoleKey id = new AccountRoleKey();

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST })
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST })
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Role role;

}
