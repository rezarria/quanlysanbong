package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false, mappedBy = "account")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<AccountRole> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {}, orphanRemoval = false, optional = true)
    @JsonIgnoreProperties("account")
    @EqualsAndHashCode.Exclude
    private User user;
}
