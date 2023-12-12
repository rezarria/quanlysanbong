package io.rezarria.sanbong.dto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.Data;

@Data
public class FieldPost {
    private String name;
    private List<String> pictures;
    private String description;
    private double price;
    private Optional<UUID> organizationId;
}
