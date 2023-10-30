package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.FieldUseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldUseHistoryRepository extends JpaRepository<FieldUseHistory, UUID> {

}
