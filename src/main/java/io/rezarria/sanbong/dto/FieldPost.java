package io.rezarria.sanbong.dto;

import java.util.Set;
import java.util.UUID;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class FieldPost {
    private String name;
    @Nullable
    private Set<String> images;
    @Nullable
    private String description;
    @Nullable
    private Double price;
    private UUID organizationId;
}
