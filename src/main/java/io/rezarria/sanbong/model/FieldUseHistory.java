package io.rezarria.sanbong.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FieldUseHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "field_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "field_detail_d")
    private FieldDetail fieldDetail;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "field_price_id")
    private FieldPrice fieldPrice;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    private String description;
}
