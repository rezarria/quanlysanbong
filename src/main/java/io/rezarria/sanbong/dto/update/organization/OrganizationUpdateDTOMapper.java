package io.rezarria.sanbong.dto.update.organization;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import io.rezarria.sanbong.model.Organization;

public abstract @Mapper(componentModel = "spring") class OrganizationUpdateDTOMapper {
    @BeanMapping(ignoreByDefault = true)
    public abstract void convert(OrganizationUpdateDTO src, @MappingTarget Organization data);

    public void patch(OrganizationUpdateDTO src, @MappingTarget Organization data) {
        convert(src, data);
    }
}
