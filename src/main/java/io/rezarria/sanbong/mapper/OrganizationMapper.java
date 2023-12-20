package io.rezarria.sanbong.mapper;

import org.mapstruct.Mapper;

import io.rezarria.sanbong.dto.post.OrganizationPostDTO;
import io.rezarria.sanbong.model.Organization;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    Organization convert(OrganizationPostDTO dto);
}
