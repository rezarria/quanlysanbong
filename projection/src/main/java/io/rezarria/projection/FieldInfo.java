package io.rezarria.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface FieldInfo {
    UUID getId();

    String getName();

    @Value("#{target.images.![path]}")
    List<String> getImages();

    @Value("#{target.prices != null ? target.prices.![price] : null}")
    List<Double> getPrices();

    String getDescription();

    @Value("#{target.organization != null ? target.organization.id : null}")
    UUID getOrganization();

    @Value("#{target.price != null ? target.price.price : null}")
    Double getPrice();

    @Value("#{target.price != null ? target.price.id : null}")
    UUID getPriceId();
}
