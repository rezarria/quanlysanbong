package io.rezarria.sanbong.mapper.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;

import io.rezarria.sanbong.mapper.ProductMapper;

@MapperConfig(componentModel = "spirng", uses = {
        ProductMapper.class }, mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public abstract class ProductUpdateDTOConfig {

}
