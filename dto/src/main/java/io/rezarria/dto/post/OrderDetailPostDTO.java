package io.rezarria.dto.post;

import java.util.UUID;

public record OrderDetailPostDTO(UUID consumerProductId, UUID priceId, int count) {

}
