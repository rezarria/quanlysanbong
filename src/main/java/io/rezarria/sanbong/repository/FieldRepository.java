package io.rezarria.sanbong.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.rezarria.sanbong.model.Field;

public interface FieldRepository extends JpaRepository<Field, UUID> {
    void deleteByName(String name);

    List<Field> findAllByNameIn(Collection<String> name);

    void deleteAllByNameIn(Collection<String> name);

    @EntityGraph(attributePaths = { "customer", "fieldPrice" })
    @Query("select u from Field u")
    <T> Stream<T> findAllStream(Class<T> typeClass);

    <T> Stream<T> findAllByNameContaining(String name, Class<T> typeClass);
}
