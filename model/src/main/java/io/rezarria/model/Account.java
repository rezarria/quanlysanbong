package io.rezarria.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.LinkedList;
import java.util.List;

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
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "account")
    @Fetch(FetchMode.SELECT)
    @Builder.Default
    private List<AccountRole> roles = new LinkedList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Fetch(FetchMode.SELECT)
    private Organization organization;
}
