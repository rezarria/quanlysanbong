package io.rezarria.projection;

import java.util.UUID;

/**
 * Projection for {@link io.rezarria.sanbong.model.Customer}
 */
public interface CustomerInfo {
    UUID getId();

    String getName();

}