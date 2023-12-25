package io.rezarria.sanbong.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FieldUnitSetting extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    private boolean unitStyle;
    private String unitName;
    private long duration;
    private long openTime;
    private long closeTime;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @OneToOne(mappedBy = "currentUnitSetting")
    private Field currentField;
}
