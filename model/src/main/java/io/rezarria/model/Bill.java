package io.rezarria.model;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
public class Bill extends BaseEntity {

    public enum PaymentMethod {
        MONEY, VNPAY, ETC
    }

    public enum PaymentStatus {
        PENDING, DONE, ERROR, CANCEL
    }

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
}
