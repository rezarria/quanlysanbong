package io.rezarria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {
    private String name;

    private String address;

    @Email
    private String email;

    private String phone;

    private String image;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "organization")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Product> products;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "organization")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Account> accounts;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "organization")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<User> users;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Fetch(FetchMode.SELECT)
    private FieldUnitSetting currentFieldUnitSetting;

    @OneToMany(mappedBy = "organization")
    @Fetch(FetchMode.SUBSELECT)
    private Set<FieldUnitSetting> fieldUnitSettings = new LinkedHashSet<>();

}
