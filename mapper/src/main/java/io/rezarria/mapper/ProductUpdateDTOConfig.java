package io.rezarria.mapper;


import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;

@MapperConfig(componentModel = "spirng", uses = {
        ProductMapper.class}, mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public abstract class ProductUpdateDTOConfig {

}
