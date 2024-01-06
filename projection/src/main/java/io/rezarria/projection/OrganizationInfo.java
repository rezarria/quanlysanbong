package io.rezarria.projection;

import java.util.UUID;

public interface OrganizationInfo {
    UUID getId();

    String getName();

    String getPhone();

    String getEmail();

    String getImage();
}