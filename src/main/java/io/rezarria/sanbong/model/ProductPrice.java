package io.rezarria.sanbong.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice extends BaseEntity {
    private double price;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = { CascadeType.DETACH, CascadeType.REFRESH })
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;
}
