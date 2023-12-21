package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import java.util.stream.Stream;

public interface RoleRepository extends JpaRepository<Role, UUID>, CustomRepository {

    @Query("select r from Role r")
    Stream<IdOnly> find();

    <T> Stream<T> findAllByNameContaining(String name);

    default boolean areIdsExist(Iterable<UUID> ids) {
        return areIdsExist(ids, Role.class);
    }

    interface IdOnly {
        UUID id();
    }
}
