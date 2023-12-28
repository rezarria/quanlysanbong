package io.rezarria.sanbong.projection;

import io.rezarria.sanbong.projection.OrganizationInfo;

/**
 * Projection for {@link io.rezarria.sanbong.model.Account}
 */
public interface AccountInfo {
    OrganizationInfo getOrganization();
}