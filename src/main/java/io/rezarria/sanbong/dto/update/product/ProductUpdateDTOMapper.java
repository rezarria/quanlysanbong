package io.rezarria.sanbong.dto.update.product;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.Product;
import io.rezarria.sanbong.model.ProductPrice;
import io.rezarria.sanbong.repository.ProductPriceRepository;
import io.rezarria.sanbong.repository.ProductRepository;
import jakarta.persistence.EntityManager;

@Mapper(componentModel = "spring")
public abstract class ProductUpdateDTOMapper {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private EntityManager entityManager;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "price", source = "price", ignore = true)
    @Mapping(target = "images", source = "images", ignore = true)
    @Mapping(target = "prices", ignore = true)
    public abstract void convert(ProductUpdateDTO src, @MappingTarget Product data);

    public void patch(ProductUpdateDTO src, @MappingTarget Product data) {
        if (data.getPrice() == null || src.getPrice() != data.getPrice().getPrice()) {
            ProductPrice price = ProductPrice.builder().price(src.getPrice())
                    .product(Field.builder().id(data.getId()).build()).build();
            entityManager.persist(price);
            data.getPrices().add(price);
        }
        convert(src, data);
    }

}