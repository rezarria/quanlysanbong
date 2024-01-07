package io.rezarria.repository;

import io.rezarria.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {
}
