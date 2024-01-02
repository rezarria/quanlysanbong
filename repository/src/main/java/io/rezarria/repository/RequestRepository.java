package io.rezarria.repository;

import io.rezarria.model.Request;
import io.rezarria.repository.interfaces.CustomRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

public interface RequestRepository extends CustomRepository<Request, UUID> {
    @Query("select u from Request u where u.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);
}
