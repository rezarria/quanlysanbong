package io.rezarria.sanbong.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public class Product extends BaseEntity {
    protected String name;
    protected String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    protected List<ProductPrice> prices;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id", unique = true, nullable = true, updatable = true)
    protected ProductPrice price;
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "product")
    protected List<ProductImage> images;
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REFRESH)
    protected Organization organization;
}
