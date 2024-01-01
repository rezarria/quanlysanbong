package io.rezarria.projection;

import java.util.UUID;

/**
 * Projection for {@link io.rezarria.model.Role}
 */
public interface RoleInfo {
    UUID getId();

    String getName();

    String getDisplayName();
}