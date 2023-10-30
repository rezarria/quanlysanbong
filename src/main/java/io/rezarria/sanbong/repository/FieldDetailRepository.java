package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.FieldDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldDetailRepository extends JpaRepository<FieldDetail, UUID> {

}
