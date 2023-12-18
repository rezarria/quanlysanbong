package io.rezarria.sanbong.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import io.rezarria.sanbong.dto.post.ProductPost;
import io.rezarria.sanbong.mapper.config.ProductUpdateDTOConfig;
import io.rezarria.sanbong.model.ConsumerProduct;

@Mapper(config = ProductUpdateDTOConfig.class)
public abstract class ConsumerProductMapper extends ProductMapper<ProductPost, ConsumerProduct> {
    @InheritConfiguration
    public abstract ConsumerProduct fieldDTOtoField(ProductPost dto);
}
