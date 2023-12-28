package io.rezarria.mapper;

import io.rezarria.dto.post.ProductPost;
import io.rezarria.model.Organization;
import io.rezarria.model.Product;
import io.rezarria.model.ProductImage;
import io.rezarria.model.ProductPrice;
import io.rezarria.repository.OrganizationRepository;
import jakarta.annotation.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    @Autowired
    private OrganizationRepository repository;

    @Named("mapDTOToProduct")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prices", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "organization", source = "organizationId", qualifiedByName = "mapOrganizationId")
    public abstract Product convert(ProductPost dto);

    @Named("mapFieldPrice")
    ProductPrice mapFieldPrice(Double price) {
        return ProductPrice.builder().price(price).build();
    }

    @Named("mapImages")
    protected Set<ProductImage> mapImages(@Nullable Set<String> images) {
        if (images == null)
            return new HashSet<>();
        return images.stream()
                .map(url -> ProductImage.builder().path(url).build())
                .collect(Collectors.toSet());
    }

    @Named("mapPrice")
    protected ProductPrice mapPrice(@Nullable Double price) {
        if (price == null)
            return null;
        return ProductPrice.builder().price(price).build();
    }

    @Named("mapOrganizationId")
    protected Organization mapOrganizationId(@Nullable UUID id) {
        if (id == null)
            return null;
        return repository.getReferenceById(id);

    }
}
