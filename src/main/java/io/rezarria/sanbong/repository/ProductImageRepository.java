package io.rezarria.sanbong.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.sanbong.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

}
