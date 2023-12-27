package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("select u from User u")
    <T> Streamable<T> findAllStream(Class<T> type);

    <T> Streamable<T> findAllByNameContaining(String name, Class<T> type);

    @Query("select u from User u")
    <T>
    Page<T> getPage(Pageable pageable, Class<T> type);


}
