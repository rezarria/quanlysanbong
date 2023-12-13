package io.rezarria.sanbong.dto.update;

import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import jakarta.annotation.Nullable;
import org.hibernate.mapping.Set;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.ProductImage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldUpdateDTO {
    private UUID id;
    @Nullable
    private String name;
    @Nullable
    private Double price;
    @Nullable
    private String description;
    @Nullable
    java.util.Set<UUID> images;

    public static FieldUpdateDTO create(Field field) {
        var builder = FieldUpdateDTO.builder();
        builder.id(field.getId());
        builder.description(field.getDescription());
        builder.name(field.getName());
        builder.images(field.getImages().stream().map(ProductImage::getId).collect(Collectors.toSet()));
        if (field.getPrice() != null) {
            builder.price(field.getPrice().getPrice());
        }
        return builder.build();
    }
}
