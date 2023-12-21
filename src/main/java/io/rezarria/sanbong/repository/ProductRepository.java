package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
