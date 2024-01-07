package io.rezarria.repository;

import io.rezarria.dto.update.CustomerUpdateDTO;
import io.rezarria.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("select c from Customer c ")
    <T> Page<T> getPage(Pageable pageable, Class<T> type);

    @Query("select c from Customer c where c.name like concat('%', ?1, '%')")
    <T>
    Page<T> findByNameContains(String name, Pageable pageable, Class<T> type);


    @Query("select c from Customer c inner join c.organization.accounts accounts where accounts.id = ?1")
    <T> Page<T> findByOrganization_Accounts_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("select c from Customer c inner join c.organization.accounts accounts where c.name = ?1 and accounts.id = ?2")
    <T>
    Page<T> findByNameAndOrganization_Accounts_Id(String name, UUID id, Pageable pageable, Class<T> type);


    @Query("select c from Customer c where c.organization.id = ?1")
    <T> Page<T> findByOrganization_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("select c from Customer c where c.organization.name like concat('%', ?1, '%')")
    <T> Stream<T> findByOrganization_NameContains(String name, Class<T> type);

    @Query("select c from Customer c")
    <T> Stream<T> getAllStreamProjection(Class<T> type);

    @Query("select c from Customer c where c.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);

    default Optional<CustomerUpdateDTO> getUpdateById(UUID id) {
        return findById(id).map(c -> CustomerUpdateDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .avatar(c.getAvatar())
                .dob(c.getDob())
                .data(c.getData())
                .build());
    }

}
