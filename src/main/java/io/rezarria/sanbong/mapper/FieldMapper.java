package io.rezarria.sanbong.mapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import io.rezarria.sanbong.dto.FieldPost;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.FieldPicture;
import io.rezarria.sanbong.model.FieldPrice;
import io.rezarria.sanbong.model.Organization;
import io.rezarria.sanbong.repository.OrganizationRepository;

@Mapper(componentModel = "spring")
public abstract class FieldMapper {

    @Autowired
    private OrganizationRepository repository;

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "prices", ignore = true)
    @Mapping(target = "usedHistories", ignore = true)
    @Mapping(target = "price", source = "price")
    @Mapping(target = "pictures", source = "pictures", qualifiedByName = "mapPictures")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "organization", source = "organizationId", qualifiedByName = "mapOrganizationId")
    public abstract Field fieldDTOtoField(FieldPost dto);

    FieldPrice mapFieldPrice(Double price) {
        return FieldPrice.builder().price(price).build();
    }

    @Named("mapPictures")
    protected Set<FieldPicture> mapPictures(List<String> pictures) {
        return pictures.stream()
                .map(url -> FieldPicture.builder().url(url).build())
                .collect(Collectors.toSet());
    }

    @Named("mapOrganizationId")
    protected Organization mapOrganizationId(Optional<UUID> id) {
        if (id.isEmpty())
            return null;
        return repository.getReferenceById(id.get());

    }
}
