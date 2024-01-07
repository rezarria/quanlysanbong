package io.rezarria.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
