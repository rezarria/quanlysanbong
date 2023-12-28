package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.dto.update.AccountUpdateDTO;
import io.rezarria.sanbong.interfaces.CustomRepository;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface AccountRepository extends CustomRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);

    @Query("select a from Account a where a.createdBy.username like concat('%', ?1, '%')")
    <T> Stream<T> findByCreatedBy_UsernameContains(String username, Class<T> type);

    @Query("select a from Account a where a.username like concat('%', ?1, '%') and a.user is null")
    <T> Stream<T> findByUsernameContainsAndUserNull(String username, Class<T> type);

    @Query("select a from Account a where a.user is null")
    <T> Stream<T> findByUserNull(Class<T> type);

    @Query("select u from Account u LEFT JOIN AccountRole r ON u.id = r.id.accountId")
    <T> Streamable<T> findAllStream(Class<T> type);

    @Query("select u from Account u where u.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> classType);

    @Query("select u from Account u")
    <T> Page<T> findAllProjection(Pageable pageable, Class<T> classType);

    default Optional<AccountUpdateDTO> findByIdForUpdate(UUID id) {
        var account = findById(id);
        return account.map(AccountUpdateDTO::create);
    }

    <T> Streamable<T> findAllByUsernameContaining(String name, Class<T> classType);

    @Query("select a.organization from Account a  where a.id = ?1")
    Optional<Organization> getOrganizationIdByAccountId(UUID id);

    @Query("select f from Account u inner join Organization o on o.id = u.organization.id inner join Field f on f.organization.id = o.id where u = :id")
    List<Field> getFieldsById(UUID id);
}
