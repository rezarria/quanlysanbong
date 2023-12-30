package io.rezarria.projection;

import java.util.UUID;

public interface FieldUnitSettingInfo {
    UUID getId();

    boolean isUnitStyle();

    String getUnitName();

    long getDuration();

    long getMinimumDuration();

    long getOpenTime();

    long getCloseTime();
}