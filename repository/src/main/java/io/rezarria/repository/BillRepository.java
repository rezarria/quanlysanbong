package io.rezarria.repository;

import io.rezarria.dto.update.BillUpdateDTO;
import io.rezarria.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    @Query("select b from Bill b where b.id = ?1")
    <T> Optional<T> getByIdProjection(UUID id, Class<T> type);

    @Query("select b.paymentStatus from Bill b where b.id = ?1 and b.paymentMethod = ?2")
    Optional<Bill.PaymentStatus> findByIdAndPaymentMethod(UUID id, Bill.PaymentMethod paymentMethod);

    @Query("select b from Bill b inner join b.organization.accounts accounts where accounts.id = ?1")
    <T>
    Page<T> findByOrganization_Accounts_Id(UUID id, Pageable pageable, Class<T> type);

    @Query("select b from Bill b")
    <T> Page<T> getPage(Pageable pageable, Class<T> type);

    @Transactional
    @Modifying
    @Query("update Bill b set b.url = ?1 where b.id = ?2")
    int updateUrlById(String url, UUID id);

    @Transactional(readOnly = true)
    @Query("select b.url from Bill b where b.id = ?1")
    Optional<String> getUrlById(UUID id);

    @Query("""
            select b from Bill b left join b.details details
            where details.product.name like concat('%', ?1, '%') or details.product.description like concat('%', ?1, '%') or b.customer.name like concat('%', ?1, '%') or b.fieldHistory.field.name like concat('%', ?1, '%') or b.fieldHistory.field.description like concat('%', ?1, '%') or b.description like concat('%', ?1, '%') or b.organization.name like concat('%', ?1, '%')""")
    <T>
    Page<T> findByName(String name, Pageable pageable, Class<T> type);

    @Query("""
            select b from Bill b left join b.organization.accounts accounts left join b.details details
            where accounts.id = ?1 and (b.customer.name like concat('%', ?2, '%') or details.product.name like concat('%', ?2, '%') or details.product.description like concat('%', ?2, '%') or b.fieldHistory.field.name like concat('%', ?2, '%') or b.fieldHistory.field.description like concat('%', ?2, '%') or b.description like concat('%', ?2, '%'))""")
    <T>
    Page<T> findByNameAndId(UUID id, String name, Pageable pageable, Class<T> type);

    @Query("select b from Bill b ")
    <T> Stream<T> getStream(Class<T> type);

    @Query("select b from Bill b inner join b.organization.accounts accounts where accounts.id = ?1")
    <T>
    Stream<T> getStreamByAccountId(UUID id, Class<T> type);

    default Optional<BillUpdateDTO> getUpdate(UUID id) {
        var bill = findById(id);
        return bill.map(b -> BillUpdateDTO.builder()
                .id(b.getId())
                .customerId(b.getCustomer().getId())
                .fieldId(b.getFieldHistory().getField().getId())
                .unitSettingId(b.getFieldHistory().getUnitSetting().getId())
                .from(b.getFieldHistory().getFrom())
                .to(b.getFieldHistory().getTo())
                .unitSize(b.getFieldHistory().getUnitSize())
                .paymentMethod(b.getPaymentMethod())
                .paymentStatus(b.getPaymentStatus())
                .details(b.getDetails().stream().map(
                        d -> BillUpdateDTO.BillDetailUpdateDTO
                                .builder()
                                .id(d.getId())
                                .consumerProductId(d.getProduct().getId())
                                .priceId(d.getPrice().getId())
                                .count(d.getCount())
                                .build()
                ).toList())
                .build());
    }
}
