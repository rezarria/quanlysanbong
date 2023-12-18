package io.rezarria.sanbong.dto.update.field;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.ProductImage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldUpdateDTO {
    private UUID id;
    @Nullable
    private String name;
    private double price;
    @Nullable
    private String description;
    @Nullable
    List<String> images;

    public static FieldUpdateDTO create(Field field) {
        var builder = FieldUpdateDTO.builder();
        builder.id(field.getId());
        builder.description(field.getDescription());
        builder.name(field.getName());
        builder.images(field.getImages().stream().map(ProductImage::getPath).toList());
        if (field.getPrice() != null) {
            builder.price(field.getPrice().getPrice());
        }
        return builder.build();
    }
}
