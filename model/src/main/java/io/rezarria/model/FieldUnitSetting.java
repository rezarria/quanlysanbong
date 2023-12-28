package io.rezarria.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

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
    private int minimumDuration;
    private int duration;
    private int openTime;
    private int closeTime;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @OneToOne(mappedBy = "currentUnitSetting")
    private Field currentField;

    @OneToMany(mappedBy = "unitSetting")
    @Fetch(FetchMode.SELECT)
    @Builder.Default
    private List<FieldHistory> histories = new ArrayList<>();

}
