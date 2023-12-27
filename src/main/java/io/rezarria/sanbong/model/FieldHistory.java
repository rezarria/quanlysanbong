package io.rezarria.sanbong.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FieldHistory extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "unit_setting_id")
    private FieldUnitSetting unitSetting;

    private int unitSize;

    @Column(name = "`from`")
    private Instant from;

    @Column(name = "`to`")
    private Instant to;
}
