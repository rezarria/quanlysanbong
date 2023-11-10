package io.rezarria.sanbong.service;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.repository.FieldRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldService implements IService<Field> {
    private final FieldRepository fieldRepository;
    private final EntityManager entityManager;

    @Override
    public JpaRepository<Field, UUID> getRepo() {
        return fieldRepository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Field create(String name, String picture, String description) throws IllegalArgumentException {
        return create(Field.builder().name(name).description(description).picture(picture).build());
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
