package io.rezarria.repository;

import io.rezarria.model.Role;
import io.rezarria.repository.interfaces.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import java.util.stream.Stream;

public interface RoleRepository extends CustomRepository<Role, UUID> {

    @Query("select r from Role r")
    Stream<IdOnly> getId();

    @Query("select r from Role r")
    <T> Stream<T> getStream(Class<T> type);

    <T> Stream<T> findAllByNameContaining(String name);

    interface IdOnly {
        UUID id();
    }
}
