package io.rezarria.sanbong.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {
    private String username;
    private String password;
    private boolean active;
    @OneToMany(fetch = FetchType.LAZY, cascade = {}, orphanRemoval = false, mappedBy = "account")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<AccountRole> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {}, orphanRemoval = false, optional = true)
    @JsonIgnoreProperties("account")
    @EqualsAndHashCode.Exclude
    private User user;
}
