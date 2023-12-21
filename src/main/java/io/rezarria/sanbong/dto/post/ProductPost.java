package io.rezarria.sanbong.dto.post;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class ProductPost {
    protected String name;
    @Nullable
    protected Set<String> images;
    @Nullable
    protected String description;
    @Nullable
    protected Double price;
    protected UUID organizationId;
}
