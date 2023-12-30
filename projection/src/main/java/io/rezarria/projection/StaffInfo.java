package io.rezarria.projection;

import java.util.Date;
import java.util.UUID;

public interface StaffInfo {
    UUID getId();

    String getName();

    String getAvatar();

    Date getDob();
}