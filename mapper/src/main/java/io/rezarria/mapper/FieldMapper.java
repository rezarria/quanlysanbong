package io.rezarria.mapper;

import io.rezarria.dto.post.FieldPost;
import io.rezarria.model.Field;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = ProductUpdateDTOConfig.class, uses = {
        ProductMapper.class })
public abstract class FieldMapper {
    @InheritConfiguration(name = "convert")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", ignore = true)
    public abstract Field convert(FieldPost dto);
}
