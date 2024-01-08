package io.rezarria.model;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class Cart extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", orphanRemoval = true)
    @Builder.Default
    private Set<CartDetail> details = new LinkedHashSet<>();

}
