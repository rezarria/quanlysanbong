package io.rezarria.sanbong.repository;

import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.rezarria.sanbong.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID>, CustomRepository {

    @Query("select r from Role r")
    Stream<IdOnly> find();

    <T> Stream<T> findAllByNameContaining(String name);

    interface IdOnly {
        UUID id();
    }

    default boolean areIdsExist(Iterable<UUID> ids) {
        return areIdsExist(ids, Role.class);
    }
}
