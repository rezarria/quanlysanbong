package io.rezarria.repository;

import io.rezarria.model.FieldDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldDetailRepository extends JpaRepository<FieldDetail, UUID> {

}
