package io.rezarria.sanbong.dto.post;

import lombok.Data;

import java.util.UUID;

@Data
public class FieldUnitSettingPostDTO {
    private boolean unitStyle;

    private String unitName;

    private long duration;

    private long openTime;

    private long closeTime;
    private UUID fieldId;
}
