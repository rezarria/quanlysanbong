package io.rezarria.sanbong.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

import io.rezarria.sanbong.dto.post.OrganizationPostDTO;
import io.rezarria.sanbong.model.Organization;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    @BeanMapping(ignoreByDefault = true)
    Organization convert(OrganizationPostDTO dto);
}
