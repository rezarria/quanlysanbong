package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRole extends Audit {
    @EmbeddedId
    private AccountRoleKey id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    @JsonIgnoreProperties({"roles", "hibernateLazyInitializer", "handler"})
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties({"accounts", "hibernateLazyInitializer", "handler"})

    private Role role;
}
