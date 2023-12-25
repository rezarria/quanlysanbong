package io.rezarria.sanbong.projection;

import java.util.UUID;

/**
 * Projection for {@link io.rezarria.sanbong.model.FieldUnitSetting}
 */
public interface FieldUnitSettingGetDTO {
    UUID getId();

    boolean isUnitStyle();

    String getUnitName();

    long getDuration();

    long getOpenTime();

    long getCloseTime();
}