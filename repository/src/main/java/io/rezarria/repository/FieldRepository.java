package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.dto.update.FieldUpdateDTO;
import io.rezarria.sanbong.interfaces.CustomRepository;
import io.rezarria.sanbong.model.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface FieldRepository extends CustomRepository<Field, UUID> {
    void deleteByName(String name);

    List<Field> findAllByNameIn(Collection<String> name);

    List<Field> findAllByOrganizationId(UUID id);

    void deleteAllByNameIn(Collection<String> name);

    // @EntityGraph(attributePaths = {
    // "details",
    // "detail",
    // "prices",
    // "price",
    // "usedHistories" })
    @Query("select u from Field u")
    <T> Stream<T> findAllStream(Class<T> typeClass);

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
        return field.map(FieldUpdateDTO::create);
    }

    @Query("select f from Field f where f.organization.id = ?1")
    <T>
    Page<T> findByOrganization_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("select f from Field f inner join f.organization.accounts accounts where accounts.id = ?1")
    <T>
    Page<T> findByOrganization_Accounts_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("select f from Field f")
    <T>
    Page<T> findAllCustom(Pageable pageable, Class<T> type);


    @Query("select f from Field f inner join f.organization.accounts accounts where accounts.id = ?1")
    <T>
    Stream<T> findByOrganization_Accounts_Id__Stream(UUID id, Class<T> type);

    @Query("select f from Field f")
    <T>
    Page<T> getFields(Pageable pageable, Class<T> type);


    <T> Streamable<T> findAllByNameContaining(String name, Class<T> typeClass);
}
