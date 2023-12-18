package io.rezarria.sanbong.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import io.rezarria.sanbong.dto.update.field.FieldUpdateDTO;
import io.rezarria.sanbong.model.Field;

public interface FieldRepository extends JpaRepository<Field, UUID> {
    void deleteByName(String name);

    List<Field> findAllByNameIn(Collection<String> name);

    void deleteAllByNameIn(Collection<String> name);

    // @EntityGraph(attributePaths = {
    // "details",
    // "detail",
    // "prices",
    // "price",
    // "usedHistories" })
    @Query("select u from Field u")
    <T> Streamable<T> findAllStream(Class<T> typeClass);

    // @EntityGraph(attributePaths = {
    // "details",
    // "detail",
    // "prices",
    // "price",
    // "images",
    // "usedHistories" })
    @Query("select u from Field u where u.id = ?1")
    <T> Optional<T> findByIdProject(UUID id, Class<T> typeClass);

    default Optional<FieldUpdateDTO> findByIdForUpdate(UUID id) {
        var field = findById(id);
        if (field.isEmpty())
            return Optional.empty();
        return Optional.of(FieldUpdateDTO.create(field.get()));
    }

    <T> Streamable<T> findAllByNameContaining(String name, Class<T> typeClass);
}
