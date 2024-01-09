package io.rezarria.dto.update;

import io.rezarria.model.Field;
import io.rezarria.model.ProductImage;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldUpdateDTO {
    @Nullable
    List<String> images;
    private UUID id;
    @Nullable
    private String name;
    private double price;
    @Nullable
    private String description;
    @Nullable
    private UUID organizationId;

    public static FieldUpdateDTO create(Field field) {
        var builder = FieldUpdateDTO.builder();
        builder.id(field.getId());
        builder.description(field.getDescription());
        builder.name(field.getName());
        builder.images(field.getImages().stream().map(ProductImage::getPath).toList());
        var organization = field.getOrganization();
        if (organization != null)
            builder.organizationId(organization.getId());
        if (field.getPrice() != null) {
            builder.price(field.getPrice().getPrice());
        }
        return builder.build();
    }
}
