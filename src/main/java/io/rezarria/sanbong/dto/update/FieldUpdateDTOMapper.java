package io.rezarria.sanbong.dto.update;

import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

@Mapper
public abstract class FieldUpdateDTOMapper {

    @Autowired
    private ProductPriceRepository repository;

    @Autowired
    private FieldRepository fieldRepository;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "price", source = "price", ignore = true)
    @Mapping(target = "images", source = "images", ignore = true)
    public abstract void convert(FieldUpdateDTO src, @MappingTarget Field data);

    public void patch(FieldUpdateDTO src, @MappingTarget Field data) {
        if (data.getPrice() != null && src.getPrice() != data.getPrice().getPrice()) {
            ProductPrice price = ProductPrice.builder().price(src.getPrice()).product(data).build();
            data.getPrices().add(price);
            fieldRepository.save(data);
            data.setPrice(price);
        }
        var imageList = data.getImages();
        imageList.removeIf(i -> !src.images.contains(i.getId()));
        var idList = imageList.stream().map(ProductImage::getId).toList();
        var newImageList = src.images.stream().filter(i -> !idList.contains(i)).toList();
        if (!newImageList.isEmpty()) {
            imageList.addAll(newImageList.stream().map(i -> ProductImage.builder().id(i).build()).toList());
        }
        convert(src, data);
    }

}
