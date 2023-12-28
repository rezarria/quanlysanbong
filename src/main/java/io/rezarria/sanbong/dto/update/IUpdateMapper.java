package io.rezarria.sanbong.dto.update;

public interface IUpdateMapper<D, T> {
    void patch(D src, T data);
}
