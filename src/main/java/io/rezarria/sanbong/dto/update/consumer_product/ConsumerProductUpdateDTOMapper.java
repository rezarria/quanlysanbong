package io.rezarria.sanbong.dto.update.consumer_product;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import io.rezarria.sanbong.mapper.config.ProductUpdateDTOConfig;
import io.rezarria.sanbong.model.ConsumerProduct;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.ProductPrice;
import io.rezarria.sanbong.repository.ConsumerProductRepository;
import io.rezarria.sanbong.repository.ProductPriceRepository;
import jakarta.persistence.EntityManager;

@Mapper(componentModel = "spring", config = ProductUpdateDTOConfig.class)
public abstract class ConsumerProductUpdateDTOMapper {

    @Autowired
    private ConsumerProductRepository consumerProductRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private EntityManager entityManager;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "price", source = "price", ignore = true)
    @Mapping(target = "images", source = "images", ignore = true)
    @Mapping(target = "prices", ignore = true)
    public abstract void convert(ConsumerProductUpdateDTO src, @MappingTarget ConsumerProduct data);

    public void patch(ConsumerProductUpdateDTO src, @MappingTarget ConsumerProduct data) {
        if (data.getPrice() == null || src.getPrice() != data.getPrice().getPrice()) {
            ProductPrice price = ProductPrice.builder().price(src.getPrice())
                    .product(Field.builder().id(data.getId()).build()).build();
            entityManager.persist(price);
            data.getPrices().add(price);
        }
        // if (src.images != null) {
        // var imageList = data.getImages();
        // imageList.removeIf(i -> !src.images.contains(i.getPath()));
        // var pathList = imageList.stream().map(ProductImage::getPath).toList();
        // var newImageList = src.images.stream().filter(i ->
        // !pathList.contains(i)).toList();
        // if (!newImageList.isEmpty()) {
        // imageList.addAll(
        // newImageList.stream().map(i ->
        // ProductImage.builder().path(i).product(data).build()).toList());
        // }
        // }
        convert(src, data);
    }

}
