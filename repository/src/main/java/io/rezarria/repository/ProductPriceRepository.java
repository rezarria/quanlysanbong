package io.rezarria.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.model.ProductPrice;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {
}
