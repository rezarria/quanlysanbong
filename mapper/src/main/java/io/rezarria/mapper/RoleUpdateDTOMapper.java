package io.rezarria.mapper;

import io.rezarria.dto.update.RoleUpdateDTO;
import io.rezarria.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class RoleUpdateDTOMapper {
    @Mapping(target = "accounts", ignore = true)
    public abstract void convert(RoleUpdateDTO src, @MappingTarget Role data);

    public void patch(RoleUpdateDTO dto, @MappingTarget Role role) {
        convert(dto, role);
    }
}
