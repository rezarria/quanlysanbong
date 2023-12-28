package io.rezarria.sanbong.mapper;

import io.rezarria.sanbong.dto.post.ConsumerProductPost;
import io.rezarria.sanbong.mapper.config.ProductUpdateDTOConfig;
import io.rezarria.sanbong.model.ConsumerProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = ProductUpdateDTOConfig.class, uses = {
        ProductMapper.class})
public abstract class ConsumerProductMapper {
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    public abstract ConsumerProduct convert(ConsumerProductPost dto);
}
