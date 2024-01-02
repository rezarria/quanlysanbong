package io.rezarria.mapper;

import io.rezarria.dto.post.OrganizationPostDTO;
import io.rezarria.model.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    Organization convert(OrganizationPostDTO dto);
}
