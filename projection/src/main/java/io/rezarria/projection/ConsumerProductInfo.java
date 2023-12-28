package io.rezarria.sanbong.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

/**
 * Projection for {@link io.rezarria.sanbong.model.ConsumerProduct}
 */
public interface ConsumerProductInfo {
    UUID getId();

    String getName();

    String getDescription();

    @Value("#{target.price != null ? target.price.price : null}")
    Double price();
}