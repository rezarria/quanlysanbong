package io.rezarria.repository;

import io.rezarria.dto.update.UserUpdateDTO;
import io.rezarria.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("select u from User u")
    <T> Streamable<T> findAllStream(Class<T> type);

    <T> Streamable<T> findAllByNameContaining(String name, Class<T> type);

    @Query("select u from User u")
    <T> Page<T> getPage(Pageable pageable, Class<T> type);

    @Query("select u from User u where u.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);

    @Transactional(readOnly = true)
    default Optional<UserUpdateDTO> findByIdForUpdate(UUID id) {
        return findById(id).map(UserUpdateDTO::create);
    }
}
