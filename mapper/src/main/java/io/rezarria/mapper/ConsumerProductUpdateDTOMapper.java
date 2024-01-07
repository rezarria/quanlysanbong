package io.rezarria.mapper;

import io.rezarria.dto.update.ConsumerProductUpdateDTO;
import io.rezarria.model.ConsumerProduct;
import io.rezarria.model.Field;
import io.rezarria.model.ProductPrice;
import io.rezarria.repository.ConsumerProductRepository;
import io.rezarria.repository.ProductPriceRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", config = ProductUpdateDTOConfig.class, uses = {
        ProductMapper.class
})
public abstract class ConsumerProductUpdateDTOMapper {

    @Autowired
    private ConsumerProductRepository consumerProductRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private EntityManager entityManager;

    @Mapping(target = "price", ignore = true)
    @Mapping(target = "prices", ignore = true)
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    public abstract void convert(ConsumerProductUpdateDTO src, @MappingTarget ConsumerProduct data);

    public void patch(ConsumerProductUpdateDTO src, @MappingTarget ConsumerProduct data) {
        convert(src, data);
        if (data.getPrice() == null || src.getPrice() != data.getPrice().getPrice()) {
            ProductPrice price = ProductPrice.builder().price(src.getPrice())
                    .product(Field.builder().id(data.getId()).build()).build();
            entityManager.persist(price);
            data.getPrices().add(price);
            data.setPrice(price);
        }
        var images = data.getImages();
        images.forEach(img -> {
            if (img.getProduct() == null)
                img.setProduct(data);
        });
    }

}
