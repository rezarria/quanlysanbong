package io.rezarria.sanbong.mapper.config;

import io.rezarria.sanbong.mapper.ProductMapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;

@MapperConfig(componentModel = "spirng", uses = {
        ProductMapper.class}, mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public abstract class ProductUpdateDTOConfig {

}
