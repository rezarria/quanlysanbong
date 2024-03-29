package io.rezarria.mapper;

import io.rezarria.dto.update.FieldUpdateDTO;
import io.rezarria.model.Field;
import io.rezarria.model.ProductImage;
import io.rezarria.model.ProductPrice;
import io.rezarria.repository.FieldRepository;
import io.rezarria.repository.OrganizationRepository;
import io.rezarria.repository.ProductPriceRepository;
import io.rezarria.security.component.Auth;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class FieldUpdateDTOMapper {

    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private EntityManager entityManager;

    @Named("mapImages")
    public void mapImages(@Nullable List<String> images, @MappingTarget Set<ProductImage> data) {
        if (images != null) {
            data.removeIf(i -> !images.contains(i.getPath()));
            var oldPath = data.stream().map(ProductImage::getPath).toList();
            var newPath = images.stream().filter(i -> !oldPath.contains(i));
            data.addAll(newPath.map(i -> ProductImage.builder().path(i).build()).toList());
        }

    }

    @Mapping(target = "price", source = "price", ignore = true)
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "prices", ignore = true)
    public abstract void convert(FieldUpdateDTO src, @MappingTarget Field data);

    public void patch(FieldUpdateDTO src, @MappingTarget Field data) {
        convert(src, data);
        if (data.getPrice() == null || src.getPrice() != data.getPrice().getPrice()) {
            ProductPrice price = ProductPrice.builder().price(src.getPrice()).product(Field.builder().id(data.getId()).build()).build();
            entityManager.persist(price);
            data.getPrices().add(price);
            data.setPrice(price);
        }
        data.getImages().forEach(image -> {
            if (image.getProduct() == null) {
                image.setProduct(data);
            }
        });
        var auth = new Auth();
        var organization = data.getOrganization();
        if (!auth.isLogin()) throw new RuntimeException();
        if (organization == null || organization.getId() != src.getOrganizationId()) {
            if (auth.hasRole("SUPER_ADMIN")) {
                if (src.getOrganizationId() != null)
                    data.setOrganization(organizationRepository.findById(src.getOrganizationId()).orElseThrow());
            } else
                data.setOrganization(organizationRepository.findByAccounts_Id(auth.getAccountId()).orElseThrow());
        }
    }

}
