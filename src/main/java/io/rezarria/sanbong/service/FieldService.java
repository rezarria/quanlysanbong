package io.rezarria.sanbong.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.repository.FieldRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldService implements IService<FieldRepository, Field> {
    private final FieldRepository fieldRepository;
    private final EntityManager entityManager;

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

    public void remove(String name) throws IllegalArgumentException {
        fieldRepository.deleteByName(name);
    }

    public void remove(Collection<String> names) {
        fieldRepository.deleteAllByNameIn(names);
    }
}
