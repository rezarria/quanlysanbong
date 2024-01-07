package io.rezarria.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.rezarria.model.Request;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query("select u from Request u where u.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);
}
