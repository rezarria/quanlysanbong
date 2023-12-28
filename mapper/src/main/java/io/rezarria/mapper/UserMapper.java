package io.rezarria.mapper;

import io.rezarria.dto.post.UserPostDTO;
import io.rezarria.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "data", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    User userPostDTOToUser(UserPostDTO dto);
}
