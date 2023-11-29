package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@Builder
@EqualsAndHashCode(callSuper = true)
public class AccountRole extends Audit {
    @EmbeddedId
    private AccountRoleKey id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @MapsId("account_id")
    @JoinColumn(name = "account_id")
    @JsonIgnoreProperties({ "roles", "hibernateLazyInitializer", "handler" })
    @EqualsAndHashCode.Exclude
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @MapsId("role_id")
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties({ "accounts", "hibernateLazyInitializer", "handler" })
    @EqualsAndHashCode.Exclude
    private Role role;

}
