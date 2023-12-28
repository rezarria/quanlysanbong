package io.rezarria.repository;

import io.rezarria.repository.interfaces.CustomRepository;
import io.rezarria.model.Product;

import java.util.UUID;

public interface ProductRepository extends CustomRepository<Product, UUID> {

}
