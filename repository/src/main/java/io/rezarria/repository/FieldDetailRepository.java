package io.rezarria.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.model.FieldDetail;

public interface FieldDetailRepository extends JpaRepository<FieldDetail, UUID> {

}
