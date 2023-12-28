package io.rezarria.mapper;

import io.rezarria.dto.post.FieldUnitSettingPostDTO;
import io.rezarria.model.Field;
import io.rezarria.model.FieldUnitSetting;
import io.rezarria.repository.FieldRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class FieldUnitSettingMapper {
    @Autowired
    private FieldRepository fieldRepository;

    @Mapping(target = "field", source = "fieldId", qualifiedByName = "mapField")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentField", ignore = true)
    @Mapping(target = "organization", ignore = true)
    public abstract FieldUnitSetting convert(FieldUnitSettingPostDTO dto);

    @Named("mapField")
    public Field mapField(UUID id) {
        var r = fieldRepository.findById(id);
        if (r.isPresent())
            return r.get();
        return null;
    }
}
