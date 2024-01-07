package io.rezarria.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface ConsumerProductInfo {
    UUID getId();

    String getName();

    @Value("#{target.images.![path]}")
    List<String> getImages();

    String getDescription();

    @Value("#{target.price != null ? target.price.price : null}")
    Double getPrice();

    @Value("#{target.price != null ? target.price.id : null}")
    UUID getPriceId();
}