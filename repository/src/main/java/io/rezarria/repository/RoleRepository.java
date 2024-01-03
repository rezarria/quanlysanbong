package io.rezarria.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import io.rezarria.dto.update.RoleUpdateDTO;
import io.rezarria.model.Role;
import io.rezarria.repository.interfaces.CustomRepository;

public interface RoleRepository extends CustomRepository<Role, UUID> {

    @Query("select r from Role r")
    Stream<IdOnly> getId();

    @Query("select r from Role r")
    <T> Stream<T> getStream(Class<T> type);

    <T> Stream<T> findAllByNameContaining(String name);

    interface IdOnly {
        UUID id();
    }

    @Query("select r from Role r where r.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);

    @Query("select r from Role r")
    <T> Page<T> getPage(Pageable page, Class<T> type);

    @Query("select new io.rezarria.dto.update.RoleUpdateDTO(r.id, r.name, r.displayName) from Role r where r.id = ?1")
    Optional<RoleUpdateDTO> createUpdateById(UUID id);
}
