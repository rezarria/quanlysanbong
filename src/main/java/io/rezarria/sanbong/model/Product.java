package io.rezarria.sanbong.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Product extends BaseEntity {
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductPrice> prices;
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private ProductPrice price;
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductImage> images;
}
