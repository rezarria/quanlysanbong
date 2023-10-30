package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.FieldPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldPriceRepository extends JpaRepository<FieldPrice, UUID> {

}
