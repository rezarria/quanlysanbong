package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.interfaces.CustomRepository;
import io.rezarria.sanbong.model.Product;

import java.util.UUID;

public interface ProductRepository extends CustomRepository<Product, UUID> {

}
