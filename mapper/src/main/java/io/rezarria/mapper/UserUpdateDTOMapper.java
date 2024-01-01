package io.rezarria.mapper;

import io.rezarria.dto.update.UserUpdateDTO;
import io.rezarria.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class UserUpdateDTOMapper {

    @BeanMapping(ignoreByDefault = true)
    public abstract void convert(UserUpdateDTO dto, @MappingTarget User user);

    public void patch(UserUpdateDTO dto, @MappingTarget User user) {
        convert(dto, user);
    }
}
