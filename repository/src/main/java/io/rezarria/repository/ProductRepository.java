package io.rezarria.repository;

import io.rezarria.model.Product;
import io.rezarria.repository.interfaces.CustomRepository;

import java.util.UUID;

public interface ProductRepository extends CustomRepository<Product, UUID> {

}
