package io.rezarria.sanbong.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.rezarria.sanbong.dto.post.ConsumerProductPost;
import io.rezarria.sanbong.model.ConsumerProduct;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public abstract class ConsumerProductMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    public abstract ConsumerProduct fieldDTOtoField(ConsumerProductPost dto);
}
