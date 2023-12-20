package io.rezarria.sanbong.dto.update.consumer_product;

import io.rezarria.sanbong.dto.update.product.ProductUpdateDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ConsumerProductUpdateDTO extends ProductUpdateDTO {
}
