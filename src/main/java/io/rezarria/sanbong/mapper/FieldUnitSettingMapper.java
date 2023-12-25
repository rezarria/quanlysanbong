package io.rezarria.sanbong.mapper;

import io.rezarria.sanbong.dto.post.FieldUnitSettingPostDTO;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.FieldUnitSetting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface FieldUnitSettingMapper {
    @Mapping(target = "currentField", source = "fieldId", qualifiedByName = "mapField")
    FieldUnitSetting convert(FieldUnitSettingPostDTO dto);

    @Named("mapField")
    default Field mapField(UUID id) {
        return Field.builder().id(id).build();
    }
}
