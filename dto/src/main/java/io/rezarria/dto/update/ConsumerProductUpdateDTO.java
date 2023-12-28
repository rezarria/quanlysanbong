package io.rezarria.dto.update;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ConsumerProductUpdateDTO extends ProductUpdateDTO {
}
