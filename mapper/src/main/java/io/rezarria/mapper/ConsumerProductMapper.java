package io.rezarria.mapper;


import io.rezarria.dto.post.ConsumerProductPost;
import io.rezarria.model.ConsumerProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = ProductUpdateDTOConfig.class, uses = {
        ProductMapper.class})
public abstract class ConsumerProductMapper {
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    public abstract ConsumerProduct convert(ConsumerProductPost dto);
}
