package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.dto.update.account.AccountUpdateDTO;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.model.Organization;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, CustomRepository {
    Optional<Account> findByUsername(String username);

    @Query("select u from Account u LEFT JOIN AccountRole r ON u.id = r.id.accountId")
    <T> Streamable<T> findAllStream(Class<T> type);

    @Query("select u from Account u where u.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> classType);

    @Query("select u from Account u")
    <T> List<T> findAllProjection(Pageable pageable, Class<T> classType);

    default Optional<AccountUpdateDTO> findByIdForUpdate(UUID id) {
        var account = findById(id);
        return account.map(AccountUpdateDTO::create);
    }

    default boolean areIdsExist(Iterable<UUID> ids) {
        return areIdsExist(ids, Account.class);
    }

    <T> Streamable<T> findAllByUsernameContaining(String name, Class<T> classType);

    @Query("select a.organization from Account a  where a.id = ?1")
    Optional<Organization> getOrganizationIdByAccountId(UUID id);

    @Query("select f from Account u inner join Organization o on o.id = u.organization.id inner join Field f on f.organization.id = o.id where u = :id")
    List<Field> getFieldsById(UUID id);
}
