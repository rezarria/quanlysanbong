package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountRole extends Audit {
    @EmbeddedId
    private AccountRoleKey id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    @JsonIgnoreProperties({"roles", "hibernateLazyInitializer", "handler"})
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties({"accounts", "hibernateLazyInitializer", "handler"})
    private Role role;


}
