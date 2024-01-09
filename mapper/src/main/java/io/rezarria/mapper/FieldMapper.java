package io.rezarria.mapper;

import io.rezarria.dto.post.FieldPost;
import io.rezarria.model.Field;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = ProductUpdateDTOConfig.class, uses = {ProductMapper.class})
public abstract class FieldMapper {
    @InheritConfiguration(name = "convert")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    @Mapping(target = "organization", source = "organizationId", qualifiedByName = "mapOrganizationId")
    public abstract Field convert(FieldPost dto);

    @AfterMapping
    public void afterMapping(FieldPost dto, @MappingTarget Field dist) {
        dist.getImages().forEach(i -> i.setProduct(dist));
    }
}
