package io.rezarria.sanbong.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import io.rezarria.sanbong.dto.update.product.ProductUpdateDTO;
import io.rezarria.sanbong.model.ConsumerProduct;

public interface ConsumerProductRepository extends JpaRepository<ConsumerProduct, UUID>, CustomRepository {
    @Query("select u from ConsumerProduct u")
    <T> Streamable<T> findAllStream(Class<T> typeClass);

    <T> Streamable<T> findAllByNameContaining(String name, Class<T> typeClass);

    @Query("select u from ConsumerProduct u where u.id = ?1")
    <T> Optional<T> findByIdProject(UUID id, Class<T> typeClass);

    default Optional<ProductUpdateDTO> findByIdForUpdate(UUID id) {
        var product = findById(id);
        if (product.isEmpty())
            return Optional.empty();
        return Optional.of(ProductUpdateDTO.create(product.get()));
    }
}
