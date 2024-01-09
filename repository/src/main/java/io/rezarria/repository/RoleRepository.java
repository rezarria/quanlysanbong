package io.rezarria.repository;

import io.rezarria.dto.update.RoleUpdateDTO;
import io.rezarria.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("select r from Role r")
    Stream<IdOnly> getId();

    @Query("select r from Role r")
    <T> Stream<T> getStream(Class<T> type);

    <T> Stream<T> findAllByNameContaining(String name);

    @Query("select r from Role r where r.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);

    @Query("select r from Role r")
    <T> Page<T> getPage(Pageable page, Class<T> type);

    @Query("select new io.rezarria.dto.update.RoleUpdateDTO(r.id, r.name, r.displayName) from Role r where r.id = ?1")
    Optional<RoleUpdateDTO> createUpdateById(UUID id);

    default boolean existsByIdIn(Collection<UUID> ids) {
        return countByCreatedBy_IdIn(ids) == ids.size();
    }

    @Query("select count(r) from Role r where r.createdBy.id in ?1")
    long countByCreatedBy_IdIn(Collection<UUID> ids);

    @Query("select r from Role r where r.name = ?1")
    Optional<Role> findByName(String name);


    interface IdOnly {
        UUID id();
    }

}
