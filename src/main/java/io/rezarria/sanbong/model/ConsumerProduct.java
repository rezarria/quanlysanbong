package io.rezarria.sanbong.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ConsumerProduct")
public class ConsumerProduct extends Product {
    private String unit;
}
