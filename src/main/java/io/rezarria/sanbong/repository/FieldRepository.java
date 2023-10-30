package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {
    void deleteByName(String name);

    List<Field> findAllByNameIn(Collection<String> name);

    void deleteAllByNameIn(Collection<String> name);
}
