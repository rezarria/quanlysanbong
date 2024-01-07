package io.rezarria.repository;

import io.rezarria.model.FieldHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface FieldHistoryRepository extends JpaRepository<FieldHistory, UUID> {
    @Query("select count(f) from FieldHistory f where f.field.id = ?1 and f.from <= ?2 and f.to >= ?3")
    long countByField_IdAndFromLessThanEqualAndToGreaterThanEqual(UUID id, Instant from, Instant to);

    @Query("select count(f) from FieldHistory f where f.field.id = ?1 and f.from <= ?2")
    long countByField_IdAndFromLessThanEqual(UUID id, Instant from);

    @Query("select f from FieldHistory f where f.field.id = ?1 and f.from >= ?2 and f.to <= ?3")
    List<FieldHistory> findByField_IdAndFromLessThanEqualAndToGreaterThanEqual(UUID id, Instant from, Instant to);

    @Query("select f from FieldHistory f where f.field.id = ?1 and f.from >= ?2")
    <T> Stream<T> getSchedule(UUID id, Instant from, Class<T> type);

    @Query("select f from FieldHistory f where f.field.id = ?1 and f.from >= ?2")
    <T> Page<T> getSchedule(UUID id, Instant from, Pageable page, Class<T> type);

}