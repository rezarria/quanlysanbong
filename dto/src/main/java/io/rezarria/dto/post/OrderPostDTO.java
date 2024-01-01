package io.rezarria.dto.post;

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
        List<OrderDetailPostDTO> details) {
}
