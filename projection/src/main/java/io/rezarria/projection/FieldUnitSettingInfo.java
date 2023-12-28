package io.rezarria.projection;

import java.util.UUID;

/**
 * Projection for {@link io.rezarria.sanbong.model.FieldUnitSetting}
 */
public interface FieldUnitSettingInfo {
    UUID getId();

    boolean isUnitStyle();

    String getUnitName();

    long getDuration();

    long getMinimumDuration();

    long getOpenTime();

    long getCloseTime();
}