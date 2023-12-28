package io.rezarria.sanbong.mapper;

import io.rezarria.sanbong.dto.update.OrganizationUpdateDTO;
import io.rezarria.sanbong.model.Organization;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

public abstract @Mapper(componentModel = "spring") class OrganizationUpdateDTOMapper {
    @BeanMapping(ignoreByDefault = true)
    public abstract void convert(OrganizationUpdateDTO src, @MappingTarget Organization data);

    public void patch(OrganizationUpdateDTO src, @MappingTarget Organization data) {
        convert(src, data);
    }
}
