package io.rezarria.sanbong.dto.update;

import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.ProductImage;
import io.rezarria.sanbong.model.ProductPrice;
import io.rezarria.sanbong.repository.FieldRepository;
import io.rezarria.sanbong.repository.ProductPriceRepository;
import jakarta.annotation.Nullable;

@Mapper(componentModel = "spring")
public abstract class FieldUpdateDTOMapper {

    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private EntityManager entityManager;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "price", source = "price", ignore = true)
    @Mapping(target = "images", source = "images", ignore = true)
    @Mapping(target = "prices", ignore = true)
    public abstract void convert(FieldUpdateDTO src, @MappingTarget Field data);

    public void patch(FieldUpdateDTO src, @MappingTarget Field data) {
        if (data.getPrice() == null || src.getPrice() != data.getPrice().getPrice()) {
            ProductPrice price = ProductPrice.builder().product(data).build();
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
