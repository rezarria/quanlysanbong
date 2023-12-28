package io.rezarria.repository;

import io.rezarria.dto.update.ProductUpdateDTO;
import io.rezarria.model.ConsumerProduct;
import io.rezarria.repository.interfaces.CustomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ConsumerProductRepository extends CustomRepository<ConsumerProduct, UUID> {
    @Query("select u from ConsumerProduct u")
    <T> Stream<T> findAllStream(Class<T> typeClass);

    <T> Streamable<T> findAllByNameContaining(String name, Class<T> typeClass);

    @Query("select u from ConsumerProduct u where u.id = ?1")
    <T> Optional<T> findByIdProject(UUID id, Class<T> typeClass);

    @Query("select c from ConsumerProduct c inner join c.organization.accounts accounts where accounts.id = ?1")
    <T>
    Stream<T> findByOrganization_Accounts_Id(UUID id, Class<T> type);


    default Optional<ProductUpdateDTO> findByIdForUpdate(UUID id) {
        var product = findById(id);
        return product.map(ProductUpdateDTO::create);
    }
}
