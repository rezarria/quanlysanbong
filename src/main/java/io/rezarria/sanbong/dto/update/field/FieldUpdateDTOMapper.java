package io.rezarria.sanbong.dto.update.field;

import java.util.List;
import java.util.Set;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.ProductImage;
import io.rezarria.sanbong.model.ProductPrice;
import io.rezarria.sanbong.repository.FieldRepository;
import io.rezarria.sanbong.repository.ProductPriceRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;

@Mapper(componentModel = "spring")
public abstract class FieldUpdateDTOMapper {

    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private EntityManager entityManager;

    @Named("mapImages")
    public void mapImages(@Nullable List<String> images, @MappingTarget Set<ProductImage> data) {
        if (images != null) {
            data.removeIf(i -> images.contains(i.getPath()));
            var oldPath = data.stream().map(ProductImage::getPath).toList();
            var newPath = images.stream().filter(i -> !oldPath.contains(i));
            data.addAll(newPath.map(i -> ProductImage.builder().path(i).build()).toList());
        }

    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "price", source = "price", ignore = true)
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    @Mapping(target = "prices", ignore = true)
    public abstract void convert(FieldUpdateDTO src, @MappingTarget Field data);

    public void patch(FieldUpdateDTO src, @MappingTarget Field data) {
        if (data.getPrice() == null || src.getPrice() != data.getPrice().getPrice()) {
            ProductPrice price = ProductPrice.builder().price(src.getPrice())
                    .product(Field.builder().id(data.getId()).build()).build();
            entityManager.persist(price);
            data.getPrices().add(price);
        }
        convert(src, data);
        data.getImages().forEach(image -> {
            if (image.getProduct() == null) {
                image.setProduct(data);
            }
        });
    }

}
