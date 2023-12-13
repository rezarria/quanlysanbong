package io.rezarria.sanbong.model;

import jakarta.persistence.CascadeType;
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
public class ProductImage extends BaseEntity {
    private String path;
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = { CascadeType.DETACH, CascadeType.REFRESH })
    private Product product;
}
