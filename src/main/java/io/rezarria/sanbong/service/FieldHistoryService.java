package io.rezarria.sanbong.service;

import io.rezarria.sanbong.model.FieldHistory;
import io.rezarria.sanbong.repository.FieldHistoryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FieldHistoryService implements IService<FieldHistoryRepository, FieldHistory> {
    private final FieldHistoryRepository repository;
    private final EntityManager entityManager;

    @Override
    public FieldHistoryRepository getRepo() {
        return repository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public boolean isFree(UUID id) {
        var now = Instant.now();
        return repository.countByField_IdAndFromLessThanEqualAndToGreaterThanEqual(id, now, now) == 0L;
    }
}
