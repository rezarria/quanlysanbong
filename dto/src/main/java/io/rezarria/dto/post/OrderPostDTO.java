package io.rezarria.dto.post;

import io.rezarria.model.Bill;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderPostDTO(
        UUID fieldId,
        UUID fieldUnitSettingId,
        UUID priceId,
        UUID customerId,
        Instant from,
        Instant to,
        int unit,
        String description,
        Bill.PaymentMethod paymentMethod,
        List<OrderDetailPostDTO> details) {
}
