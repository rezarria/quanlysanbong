package io.rezarria.service;

import io.rezarria.model.*;
import io.rezarria.repository.BillRepository;
import io.rezarria.security.component.Auth;
import io.rezarria.service.interfaces.IService;
import io.rezarria.vnpay.Config;
import io.rezarria.vnpay.Create;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BillService extends IService<BillRepository, Bill> {
    private final BillRepository repository;
    private final EntityManager entityManager;

    @Override
    protected BillRepository getRepo() {
        return repository;
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return repository.getByIdProjection(id, type);
    }

    public Bill createOrder(Collection<OrderInfo> infoList, Customer customer, Bill.PaymentMethod method, String description) {
        var bill = Bill.builder().build();
        bill.setPaymentMethod(method);
        bill.setCustomer(customer);
        bill.setDescription(description);
        var details = bill.getDetails();
        details.addAll(
                infoList.stream()
                        .map(info -> BillDetail
                                .builder()
                                .product(info.product)
                                .price(info.price)
                                .count(info.count)
                                .build()
                        ).toList()
        );
        bill.setTotalPrice(
                infoList.stream()
                        .map(info -> info.price.getPrice() * info.count)
                        .reduce(0.0, Double::sum)
        );
        bill.setPaymentStatus(Bill.PaymentStatus.PENDING);
        repository.save(bill);
        return bill;
    }

    public Bill.PaymentStatus getVNPAYPaymentStatus(UUID id) {
        return repository.findByIdAndPaymentMethod(id, Bill.PaymentMethod.VNPAY).orElseThrow();
    }

    public String createVNPAY(UUID billId, HttpServletRequest request) {
        return createVNPAY(repository.findById(billId).orElseThrow(), Config.getIpAddress(request));
    }

    public String createVNPAY(Bill bill, String ip) {
        var url = Create.create("", Math.round(bill.getTotalPrice()), ip, "vn", bill.getId());
        repository.updateUrlById(url, bill.getId());
        return url;
    }

    public String getVNPAYUrl(UUID id) {
        return repository.getUrlById(id).orElseThrow();
    }

    public void paymentByid(UUID id, Bill.PaymentStatus status, String description) {
        var bill = repository.findById(id).orElseThrow();
        payment(bill, status, description);
    }

    @Transactional
    public void payment(Bill bill, Bill.PaymentStatus status, String description) {
        bill.setPaymentStatus(status);
        bill.setDescription(description);
        repository.save(bill);
    }

    @Transactional(readOnly = true)
    public <T> Page<T> getPage(Pageable pageable, Class<T> type) {
        var auth = new Auth();
        if (auth.isLogin()) {
            if (auth.hasRole("SUPER_ADMIN"))
                return repository.getPage(pageable, type);
            return repository.findByOrganization_Accounts_Id(auth.getAccountId(), pageable, type);
        }
        throw new RuntimeException();
    }

    public <T> Page<T> getPageContainName(String name, Pageable pageable, Class<T> type) {
        var auth = new Auth();
        if (auth.isLogin()) {
            if (auth.hasRole("SUPER_ADMIN"))
                return repository.findByName(name, pageable, type);
            return repository.findByNameAndId(auth.getAccountId(), name, pageable, type);
        }
        throw new RuntimeException();
    }

    public <T> Stream<T> getAll(Class<T> type) {
        var auth = new Auth();
        if (auth.isLogin()) {
            if (auth.hasRole("SUPER_ADMIN"))
                return repository.getStream(type);
            return repository.getStreamByAccountId(auth.getAccountId(), type);
        }
        throw new RuntimeException();
    }

    @Builder
    public record OrderInfo(Product product, ProductPrice price, long count) {
    }

    @Builder
    public record OrderInfoId(UUID productId, UUID priceId, long count) {
    }

}
