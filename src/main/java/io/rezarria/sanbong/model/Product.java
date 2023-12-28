package io.rezarria.sanbong.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public class Product extends BaseEntity {
    protected String name;

    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    protected String description;

    protected boolean active;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    protected Set<ProductPrice> prices = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "price_id", unique = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected ProductPrice price;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Set<ProductImage> images = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Organization organization;
}
