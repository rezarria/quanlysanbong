package io.rezarria.sanbong.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FieldUseHistory extends BaseEntity {
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Field field;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Staff staff;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Customer customer;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private FieldDetail fieldDetail;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private FieldPrice fieldPrice;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    private String description;
}
