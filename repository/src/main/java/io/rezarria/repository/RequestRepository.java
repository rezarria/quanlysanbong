package io.rezarria.repository;

import io.rezarria.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query("select u from Request u where u.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);
}
