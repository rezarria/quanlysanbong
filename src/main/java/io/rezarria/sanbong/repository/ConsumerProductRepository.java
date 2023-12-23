package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.dto.update.product.ProductUpdateDTO;
import io.rezarria.sanbong.model.ConsumerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ConsumerProductRepository extends JpaRepository<ConsumerProduct, UUID>, CustomRepository {
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
