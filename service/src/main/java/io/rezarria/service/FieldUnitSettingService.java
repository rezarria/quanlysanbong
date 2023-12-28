package io.rezarria.service;

import io.rezarria.model.FieldUnitSetting;
import io.rezarria.repository.FieldRepository;
import io.rezarria.repository.FieldUnitSettingRepository;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FieldUnitSettingService implements IService<FieldUnitSettingRepository, FieldUnitSetting> {
    private final FieldUnitSettingRepository repository;
    private final EntityManager entityManager;
    private final FieldRepository fieldRepository;

    @Override
    public FieldUnitSettingRepository getRepo() {
        return repository;
    }

    public <T> Optional<T> getSettingFromFieldId(UUID id, Class<T> type) {
        return repository.findByFields_Id(id, type);
    }

    public boolean setCurrentField(UUID fieldId, FieldUnitSetting setting) {
        return false;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
