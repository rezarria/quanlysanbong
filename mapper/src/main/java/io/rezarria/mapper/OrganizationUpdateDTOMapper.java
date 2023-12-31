package io.rezarria.mapper;

import io.rezarria.dto.update.OrganizationUpdateDTO;
import io.rezarria.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

public abstract @Mapper(componentModel = "spring") class OrganizationUpdateDTOMapper {
    public abstract void convert(OrganizationUpdateDTO src, @MappingTarget Organization data);

    public void patch(OrganizationUpdateDTO src, @MappingTarget Organization data) {
        convert(src, data);
    }
}
