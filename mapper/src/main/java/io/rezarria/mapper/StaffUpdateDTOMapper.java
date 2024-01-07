package io.rezarria.mapper;

import io.rezarria.dto.update.StaffUpdateDTO;
import io.rezarria.model.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class StaffUpdateDTOMapper {

    public abstract void convert(StaffUpdateDTO dto, @MappingTarget Staff staff);

    public void patch(StaffUpdateDTO dto, @MappingTarget Staff user) {
        convert(dto, user);
    }
}
