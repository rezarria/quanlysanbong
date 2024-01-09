package io.rezarria.dto.update;

import io.rezarria.model.Bill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillUpdateDTO {
    private UUID id;
    private UUID customerId;
    private UUID fieldId;
    private UUID unitSettingId;
    private Instant from;
    private Instant to;
    private String description;
    private UUID organizationId;
    private Integer unitSize;
    private Bill.PaymentMethod paymentMethod;
    private Bill.PaymentStatus paymentStatus;

    private List<BillDetailUpdateDTO> details;

    @Data

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillDetailUpdateDTO {
        private UUID id;
        private UUID consumerProductId;
        private UUID priceId;
        private Long count;
    }
}
