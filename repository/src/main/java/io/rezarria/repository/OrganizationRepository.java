package io.rezarria.repository;

import io.rezarria.dto.update.OrganizationUpdateDTO;
import io.rezarria.model.Organization;
import io.rezarria.repository.interfaces.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends CustomRepository<Organization, UUID> {
    default Optional<OrganizationUpdateDTO> findByIdForUpdate(UUID id) {
        var account = findById(id);
        return account.map(OrganizationUpdateDTO::create);
    }

    @Query("select o.id from Organization o inner join o.accounts accounts where accounts.id = ?1")
    Optional<UUID> getIdByAccountId(UUID id);

    @Query("select o from Organization o inner join o.accounts accounts where accounts.id = ?1")
    Optional<Organization> findByAccounts_Id(UUID id);

    @Query("select o from Organization o where o.id in ?1")
    <T> Page<T> findByIdIn(Collection<UUID> ids, Pageable pageable, Class<T> type);

    @Query("select o from Organization o where o.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);
}
