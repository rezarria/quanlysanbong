package io.rezarria.dto.update;

import io.rezarria.model.ConsumerProduct;
import io.rezarria.model.ProductImage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.stream.Collectors;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ConsumerProductUpdateDTO extends ProductUpdateDTO {
    public static ConsumerProductUpdateDTO create(ConsumerProduct field) {
        return ConsumerProductUpdateDTO
                .builder()
                .id(field.getId())
                .price(field.getPrice().getPrice())
                .description(field.getDescription())
                .name(field.getName())
                .images(field.getImages().stream().map(ProductImage::getPath).collect(Collectors.toSet()))
                .build();
    }
}
