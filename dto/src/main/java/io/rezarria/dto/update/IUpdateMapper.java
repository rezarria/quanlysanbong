package io.rezarria.update;

public interface IUpdateMapper<D, T> {
    void patch(D src, T data);
}