package io.rezarria.sanbong.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.rezarria.sanbong.dto.post.FieldPost;
import io.rezarria.sanbong.mapper.config.ProductUpdateDTOConfig;
import io.rezarria.sanbong.model.Field;

@Mapper(config = ProductUpdateDTOConfig.class)
public abstract class FieldMapper extends ProductMapper<FieldPost, Field> {
    @InheritConfiguration
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "usedHistories", ignore = true)
    public abstract Field fieldDTOtoField(FieldPost dto);
}
