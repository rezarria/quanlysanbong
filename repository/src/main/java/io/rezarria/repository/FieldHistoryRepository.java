package io.rezarria.repository;

import io.rezarria.repository.interfaces.CustomRepository;
import io.rezarria.model.FieldHistory;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface FieldHistoryRepository extends CustomRepository<FieldHistory, UUID> {
    @Query("select count(f) from FieldHistory f where f.field.id = ?1 and f.from <= ?2 and f.to >= ?3")
    long countByField_IdAndFromLessThanEqualAndToGreaterThanEqual(UUID id, Instant from, Instant to);

    @Query("select f from FieldHistory f where f.field.id = ?1 and f.from >= ?2 and f.to <= ?3")
    List<FieldHistory> findByField_IdAndFromLessThanEqualAndToGreaterThanEqual(UUID id, Instant from, Instant to);

}