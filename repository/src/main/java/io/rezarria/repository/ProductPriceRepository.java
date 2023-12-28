package io.rezarria.repository;

import io.rezarria.repository.interfaces.CustomRepository;
import io.rezarria.model.ProductPrice;

import java.util.UUID;

public interface ProductPriceRepository extends CustomRepository<ProductPrice, UUID> {
}
