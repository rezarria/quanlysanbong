package io.rezarria.sanbong.dto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class FieldPost {
    private String name;
    @Nullable
    private List<String> pictures;
    @Nullable
    private String description;
    @Nullable
    private Double price;
    private UUID organizationId;
}
