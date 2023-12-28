package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.interfaces.CustomRepository;
import io.rezarria.sanbong.model.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import java.util.stream.Stream;

public interface RoleRepository extends CustomRepository<Role, UUID> {

    @Query("select r from Role r")
    Stream<IdOnly> find();

    <T> Stream<T> findAllByNameContaining(String name);

    interface IdOnly {
        UUID id();
    }
}
