package io.rezarria.sanbong.projection;

import java.util.UUID;

/**
 * Projection for {@link io.rezarria.sanbong.model.Organization}
 */
public interface OrganizationInfo {
    UUID getId();

    String getName();
}