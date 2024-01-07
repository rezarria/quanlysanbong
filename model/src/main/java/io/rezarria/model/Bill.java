package io.rezarria.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
public class Bill extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    @Fetch(FetchMode.SELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<BillDetail> details = new LinkedList<>();
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    @Fetch(FetchMode.SELECT)
    private FieldHistory fieldHistory;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private double totalPrice;
    private String url;
    private String description;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public enum PaymentMethod {
        MONEY, VNPAY, ETC
    }

    public enum PaymentStatus {
        NONE, PENDING, DONE, ERROR, CANCEL
    }
}
