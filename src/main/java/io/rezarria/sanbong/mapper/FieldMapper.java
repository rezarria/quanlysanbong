package io.rezarria.sanbong.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.rezarria.sanbong.dto.post.FieldPost;
import io.rezarria.sanbong.model.Field;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public abstract class FieldMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "usedHistories", ignore = true)
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    public abstract Field fieldDTOtoField(FieldPost dto);
}
