package io.rezarria.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.rezarria.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, UUID> {
    @Query("select s from Staff s inner join s.organization.accounts accounts where accounts.id = ?1")
    <T> Page<T> findByOrganization_Accounts_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("select s from Staff s where s.organization.id = ?1")
    <T> Page<T> findByOrganization_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("select s from Staff s")
    <T> Page<T> getAllProjection(Pageable page, Class<T> type);

    @Query("select s from Staff s where s.id = ?1")
    <T> Optional<T> getByIdProjection(UUID id, Class<T> type);

    @Query("select s from Staff s inner join s.organization.accounts accounts where s.id = ?1 and accounts.id = ?2")
    <T> Optional<T> findByIdAndOrganization_Accounts_Id(UUID id, UUID id1, Class<T> type);

    @Query("select s from Staff s where s.account.id = ?1")
    Optional<Staff> findByAccount_Id(UUID id);

    @Query("select s from Staff s")
    <T> Stream<T> getAllStreamProjection(Class<T> type);

    @Query("select s from Staff s inner join s.organization.accounts accounts where accounts.id = ?1")
    <T> Stream<T> getAllStreamProjectionByAccountId(UUID id, Class<T> type);

}
