package io.rezarria.sanbong.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import io.rezarria.sanbong.dto.post.ProductPost;
import io.rezarria.sanbong.model.Organization;
import io.rezarria.sanbong.model.Product;
import io.rezarria.sanbong.model.ProductImage;
import io.rezarria.sanbong.model.ProductPrice;
import io.rezarria.sanbong.repository.OrganizationRepository;
import jakarta.annotation.Nullable;

public abstract class ProductMapper<DTO extends ProductPost, ENTITY extends Product> {
    @Autowired
    private OrganizationRepository repository;

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prices", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", source = "images", qualifiedByName = "mapPictures")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "organization", source = "organizationId", qualifiedByName = "mapOrganizationId")
    public abstract ENTITY fieldDTOtoField(DTO dto);

    ProductPrice mapFieldPrice(Double price) {
        return ProductPrice.builder().price(price).build();
    }

    @Named("mapPictures")
    protected Set<ProductImage> mapPictures(@Nullable Set<String> pictures) {
        if (pictures == null)
            return new HashSet<>();
        return pictures.stream()
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
