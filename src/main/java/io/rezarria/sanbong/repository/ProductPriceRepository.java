package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {
}
