package io.rezarria.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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

        @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "organization")
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private List<Product> products;

        @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "organization")
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private List<Account> accounts;

        @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "organization")
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private List<User> users;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn
        @Fetch(FetchMode.SELECT)
        private FieldUnitSetting currentFieldUnitSetting;

        @OneToMany(mappedBy = "organization")
        @Fetch(FetchMode.SUBSELECT)
        @Builder.Default
        private Set<FieldUnitSetting> fieldUnitSettings = new LinkedHashSet<>();

}
