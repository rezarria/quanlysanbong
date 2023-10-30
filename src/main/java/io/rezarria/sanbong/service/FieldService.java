package io.rezarria.sanbong.service;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.repository.FieldRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldService {
    private final FieldRepository fieldRepository;

    public Field create(String name, String picture, String description) throws IllegalArgumentException {
        return fieldRepository.save(Field.builder().name(name).description(description).picture(picture).build());
    }

    public List<Field> getAll() {
        return fieldRepository.findAll();
    }

    public Iterable<Field> createMany(Iterable<Field> fields) {
        return fieldRepository.saveAll(fields);
    }

    public Field get(UUID id) {
        return fieldRepository.getReferenceById(id);
    }

    public List<Field> getMany(Collection<UUID> ids) {
        return fieldRepository.findAllById(ids);
    }

    public List<Field> getManyByName(Collection<String> names) {
        return fieldRepository.findAllByNameIn(names);
    }

    public void remove(UUID id) throws IllegalArgumentException {
        fieldRepository.deleteById(id);
    }

    public void remove(String name) throws IllegalArgumentException {
        fieldRepository.deleteByName(name);
    }

    public void remove(Collection<String> names) {
        fieldRepository.deleteAllByNameIn(names);
    }

    public Field update(Field field) {
        return fieldRepository.save(field);
    }
}
