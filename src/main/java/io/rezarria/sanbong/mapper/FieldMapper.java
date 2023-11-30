package io.rezarria.sanbong.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.rezarria.sanbong.dto.FieldPost;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.FieldPrice;

@Mapper(componentModel = "spring")
public abstract class FieldMapper {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fieldDetail", ignore = true)
    @Mapping(target = "fieldDetails", ignore = true)
    @Mapping(target = "fieldPrices", ignore = true)
    @Mapping(target = "fieldUseHistories", ignore = true)
    @Mapping(target = "picture", ignore = true)
    @Mapping(target = "fieldPrice", source = "price")
    public abstract Field fieldDTOtoField(FieldPost dto);

    FieldPrice mapFieldPrice(Double price) {
        return FieldPrice.builder().price(price).build();
    }
}
