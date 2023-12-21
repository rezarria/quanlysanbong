package io.rezarria.sanbong.service;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.repository.FieldRepository;
import io.rezarria.sanbong.security.component.Auth;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldService implements IService<FieldRepository, Field> {
    private final FieldRepository fieldRepository;
    private final EntityManager entityManager;
    private final Auth auth;

    @Override
    public FieldRepository getRepo() {
        return fieldRepository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Field> getManyByName(Collection<String> names) {
        return fieldRepository.findAllByNameIn(names);
    }

    public List<Field> getFieldsByOrganizationId(UUID id) {
        return fieldRepository.findAllByOrganizationId(id);
    }

    public void remove(String name) throws IllegalArgumentException {
        fieldRepository.deleteByName(name);
    }

    public void remove(Collection<String> names) {
        fieldRepository.deleteAllByNameIn(names);
    }

    @SneakyThrows
    public <T> List<T> getFieldsByName(String name, Class<T> classType) {
        if (auth.isLogin()) {
            return null;
        } else {
            throw new PermissionDeniedDataAccessException("field", null);
        }
    }

}
