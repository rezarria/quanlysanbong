package io.rezarria.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
public class Bill extends BaseEntity {

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "bill_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<BillDetail> details;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private double totalPrice;
    private String description;
    public enum PaymentMethod {
        MONEY, VNPAY, ETC
    }
    public enum PaymentStatus {
        PENDING, DONE, ERROR, CANCEL
    }
}