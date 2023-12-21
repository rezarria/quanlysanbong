package io.rezarria.sanbong.mapper;

import io.rezarria.sanbong.dto.post.OrganizationPostDTO;
import io.rezarria.sanbong.model.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    Organization convert(OrganizationPostDTO dto);
}
