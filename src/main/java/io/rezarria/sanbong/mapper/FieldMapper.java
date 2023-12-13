package io.rezarria.sanbong.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import io.micrometer.common.lang.Nullable;
import io.rezarria.sanbong.dto.FieldPost;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.Organization;
import io.rezarria.sanbong.model.ProductImage;
import io.rezarria.sanbong.model.ProductPrice;
import io.rezarria.sanbong.repository.OrganizationRepository;

@Mapper(componentModel = "spring")
public abstract class FieldMapper {

    @Autowired
    private OrganizationRepository repository;

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "prices", ignore = true)
    @Mapping(target = "usedHistories", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", source = "pictures", qualifiedByName = "mapPictures")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "organization", source = "organizationId", qualifiedByName = "mapOrganizationId")
    public abstract Field fieldDTOtoField(FieldPost dto);

    ProductPrice mapFieldPrice(Double price) {
        return ProductPrice.builder().price(price).build();
    }

    @Named("mapPictures")
    protected List<ProductImage> mapPictures(@Nullable List<String> pictures) {
        if (pictures == null)
            return new ArrayList<>();
        return pictures.stream()
                .map(url -> ProductImage.builder().path(url).build())
                .collect(Collectors.toList());
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
