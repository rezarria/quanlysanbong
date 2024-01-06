package io.rezarria.dto.update;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.rezarria.model.Product;
import io.rezarria.model.ProductImage;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {
    protected UUID id;
    @Nullable
    protected String name;
    protected double price;
    @Nullable
    protected String description;
    @Nullable
    protected Set<String> images;

    public static ProductUpdateDTO create(Product field) {
        var builder = ProductUpdateDTO.builder();
        builder.id(field.getId());
        builder.description(field.getDescription());
        builder.name(field.getName());
        builder.images(field.getImages().stream().map(ProductImage::getPath).collect(Collectors.toSet()));
        if (field.getPrice() != null) {
            builder.price(field.getPrice().getPrice());
        }
        return builder.build();
    }
}
