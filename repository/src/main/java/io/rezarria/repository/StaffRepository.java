package io.rezarria.repository;

import io.rezarria.dto.update.StaffUpdateDTO;
import io.rezarria.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface StaffRepository extends JpaRepository<Staff, UUID> {
    @Query("select s from Staff s inner join s.organization.accounts accounts where accounts.id = ?1")
    <T> Page<T> findByOrganization_Accounts_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("""
            select s from Staff s inner join s.organization.accounts accounts
            where s.name like concat('%', ?1, '%') and accounts.id = ?2""")
    <T>
    Page<T> findByNameContainsAndOrganization_Accounts_Id(String name, UUID id, Pageable pageable, Class<T> type);

    @Query("select s from Staff s where s.name like concat('%', ?1, '%')")
    <T>
    Page<T> findByNameContains(String name, Pageable pageable, Class<T> type);

    @Query("select s from Staff s ")
    <T> Page<T> getPage(Pageable pageable, Class<T> type);

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


    default Optional<StaffUpdateDTO> getUpdateById(UUID id) {
        return findById(id).map(t -> StaffUpdateDTO.builder()
                .id(t.getId())
                .name((t.getName()))
                .avatar(t.getAvatar())
                .dob(t.getDob())
                .build());
    }
}
