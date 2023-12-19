package io.rezarria.sanbong.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.rezarria.sanbong.dto.post.FieldPost;
import io.rezarria.sanbong.mapper.config.ProductUpdateDTOConfig;
import io.rezarria.sanbong.model.Field;

@Mapper(componentModel = "spring", config = ProductUpdateDTOConfig.class, uses = {
        ProductMapper.class })
public abstract class FieldMapper {
    @InheritConfiguration(name = "convert")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "usedHistories", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    public abstract Field convert(FieldPost dto);
}
