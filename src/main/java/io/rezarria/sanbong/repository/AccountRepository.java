package io.rezarria.sanbong.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import io.rezarria.sanbong.model.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);

    @Query("select u from Account u LEFT JOIN AccountRole r ON u.id = r.id.accountId")
    <T> Streamable<T> findAllStream(Class<T> type);

    <T> Streamable<T> findAllByUsernameContaining(String name, Class<T> classType);
}
