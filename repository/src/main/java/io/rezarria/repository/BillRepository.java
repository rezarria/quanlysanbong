package io.rezarria.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import io.rezarria.model.Bill;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    @Query("select b from Bill b where b.id = ?1")
    <T> Optional<T> getByIdProjection(UUID id, Class<T> type);

    @Query("select b.paymentStatus from Bill b where b.id = ?1 and b.paymentMethod = ?2")
    Optional<Bill.PaymentStatus> findByIdAndPaymentMethod(UUID id, Bill.PaymentMethod paymentMethod);

    @Transactional
    @Modifying
    @Query("update Bill b set b.url = ?1 where b.id = ?2")
    int updateUrlById(String url, UUID id);

    @Transactional(readOnly = true)
    @Query("select b.url from Bill b where b.id = ?1")
    Optional<String> getUrlById(UUID id);

}
