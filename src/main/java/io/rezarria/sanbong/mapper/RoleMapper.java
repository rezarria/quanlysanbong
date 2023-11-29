package io.rezarria.sanbong.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.rezarria.sanbong.dto.RolePostDTO;
import io.rezarria.sanbong.model.Role;

@Mapper(componentModel = "spring")

public interface RoleMapper {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "registerTemplates", ignore = true)

    Role rolePostDTOToRole(RolePostDTO dto);
}
