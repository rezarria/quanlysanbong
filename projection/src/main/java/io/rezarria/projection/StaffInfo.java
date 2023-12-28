package io.rezarria.sanbong.projection;

import java.util.Date;
import java.util.UUID;

/**
 * Projection for {@link io.rezarria.sanbong.model.Staff}
 */
public interface StaffInfo {
    UUID getId();

    String getName();

    String getAvatar();

    Date getDob();
}