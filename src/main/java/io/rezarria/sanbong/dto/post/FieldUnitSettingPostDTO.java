package io.rezarria.sanbong.dto.post;

import java.util.UUID;

import lombok.Data;

@Data
public class FieldUnitSettingPostDTO {
    private boolean unitStyle;

    private String unitName;

    private int duration;
    private int minimumDuration;

    private int openTime;

    private int closeTime;
    private UUID fieldId;
}
