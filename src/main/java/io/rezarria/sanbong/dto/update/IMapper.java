package io.rezarria.sanbong.dto.update;

import org.mapstruct.MappingTarget;

public interface IMapper<D, T> {
    void patch(D src, T data);
}
