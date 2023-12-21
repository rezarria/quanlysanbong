package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {
    private String name;
    private String address;
    private String email;
    private String phone;
    private String image;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "organization")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Product> products;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "organization")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Account> accounts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "organization")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<User> users;
}
