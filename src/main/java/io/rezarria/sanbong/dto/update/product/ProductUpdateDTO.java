package io.rezarria.sanbong.dto.update.product;

import java.util.List;
import java.util.UUID;

import org.mapstruct.MappingTarget;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.Product;
import io.rezarria.sanbong.model.ProductImage;
import io.rezarria.sanbong.model.ProductPrice;
import lombok.Builder;
import lombok.Data;

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
    protected List<String> images;

    public static ProductUpdateDTO create(Product field) {
        var builder = ProductUpdateDTO.builder();
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
