package io.rezarria.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import io.rezarria.dto.post.ConsumerProductPost;
import io.rezarria.model.ConsumerProduct;

@Mapper(componentModel = "spring", config = ProductUpdateDTOConfig.class, uses = {
        ProductMapper.class })
public abstract class ConsumerProductMapper {
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    public abstract ConsumerProduct convert(ConsumerProductPost dto);

    @AfterMapping
    public void afterMapping(ConsumerProductPost dto, @MappingTarget ConsumerProduct product) {
        var images = product.getImages();
        images.forEach(i -> {
            i.setProduct(product);
        });
    }
}
