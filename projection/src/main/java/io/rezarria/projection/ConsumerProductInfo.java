package io.rezarria.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public interface ConsumerProductInfo {
    UUID getId();

    String getName();

    String getDescription();

    @Value("#{target.price != null ? target.price.price : null}")
    Double price();
}