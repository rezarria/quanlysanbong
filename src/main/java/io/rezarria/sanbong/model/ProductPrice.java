package io.rezarria.sanbong.model;

import org.modelmapper.internal.bytebuddy.dynamic.TypeResolutionStrategy.Lazy;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
public class ProductPrice extends BaseEntity {
    private double price;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;
}
