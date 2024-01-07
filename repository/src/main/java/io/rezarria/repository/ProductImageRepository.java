package io.rezarria.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

}
