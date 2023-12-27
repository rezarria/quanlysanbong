package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query("select c from Customer c inner join c.organization.accounts accounts where accounts.id = ?1")
    <T>
    Page<T> findByOrganization_Accounts_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("select c from Customer c where c.organization.id = ?1")
    <T>
    Page<T> findByOrganization_Id(UUID id, Pageable pageable, Class<T> type);

}
