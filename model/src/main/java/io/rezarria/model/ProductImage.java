package io.rezarria.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage extends BaseEntity {
    private String path;
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;
}
