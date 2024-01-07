package io.rezarria.mapper;

import io.rezarria.dto.update.CustomerUpdateDTO;
import io.rezarria.dto.update.UserUpdateDTO;
import io.rezarria.model.Customer;
import io.rezarria.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CustomerUpdateDTOMapper {

    public abstract void convert(CustomerUpdateDTO dto, @MappingTarget Customer customer);

    public void patch(CustomerUpdateDTO dto, @MappingTarget Customer customer) {
        convert(dto, customer);
    }
}
