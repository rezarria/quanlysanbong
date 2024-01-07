package io.rezarria.repository;

import io.rezarria.dto.update.ProductUpdateDTO;
import io.rezarria.model.ConsumerProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ConsumerProductRepository extends JpaRepository<ConsumerProduct, UUID> {
    @Query("select u from ConsumerProduct u")
    <T> Stream<T> findAllStream(Class<T> typeClass);

    <T> Stream<T> findAllByNameContaining(String name, Class<T> typeClass);

    @Query("""
            select c from ConsumerProduct c
            where c.name like concat('%', ?1, '%') and c.organization.id = ?2""")
    <T> Stream<T> findByNameContainsAndCreatedBy_ActiveTrueAndOrganization_Id(String name, UUID id, Class<T> type);

    @Query("select u from ConsumerProduct u where u.id = ?1")
    <T> Optional<T> findByIdProject(UUID id, Class<T> typeClass);

    @Query("select c from ConsumerProduct c inner join c.organization.accounts accounts where accounts.id = ?1")
    <T> Stream<T> findByOrganization_Accounts_Id(UUID id, Class<T> type);

    @Query("select c from ConsumerProduct c")
    <T> Page<T> getPage(Pageable page, Class<T> type);

    default Optional<ProductUpdateDTO> findByIdForUpdate(UUID id) {
        var product = findById(id);
        return product.map(ProductUpdateDTO::create);
    }
}
