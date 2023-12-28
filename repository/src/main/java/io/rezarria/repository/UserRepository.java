package io.rezarria.repository;

import io.rezarria.model.User;
import io.rezarria.repository.interfaces.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.UUID;

public interface UserRepository extends CustomRepository<User, UUID> {
    @Query("select u from User u")
    <T> Streamable<T> findAllStream(Class<T> type);

    <T> Streamable<T> findAllByNameContaining(String name, Class<T> type);

    @Query("select u from User u")
    <T>
    Page<T> getPage(Pageable pageable, Class<T> type);
}
