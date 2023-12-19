package io.rezarria.sanbong.dto.post;

import java.util.Set;
import java.util.UUID;

import jakarta.annotation.Nullable;
import lombok.Data;

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
