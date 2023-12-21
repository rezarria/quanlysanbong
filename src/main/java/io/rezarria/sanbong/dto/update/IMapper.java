package io.rezarria.sanbong.dto.update;

public interface IMapper<D, T> {
    void patch(D src, T data);
}
