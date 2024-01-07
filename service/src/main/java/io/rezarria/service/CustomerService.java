package io.rezarria.service;

import io.rezarria.dto.update.CustomerUpdateDTO;
import io.rezarria.model.Customer;
import io.rezarria.repository.CustomerRepository;
import io.rezarria.security.component.Auth;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomerService extends IService<CustomerRepository, Customer> {
    private final CustomerRepository repository;
    private final EntityManager entityManager;

    @Override
    public CustomerRepository getRepo() {
        return repository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional(readOnly = true)
    public <T> Stream<T> findAllByName(String name, Class<T> type) {
        return repository.findByOrganization_NameContains(name, type);
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

    @Transactional(readOnly = true)
    public <T> Page<T> getPageContainName(String name, Pageable pageable, Class<T> type) {
        var auth = new Auth();
        if (auth.isLogin()) {
            if (auth.hasRole("SUPER_ADMIN"))
                return repository.findByNameContains(name, pageable, type);
            return repository.findByNameAndOrganization_Accounts_Id(name, auth.getAccountId(), pageable, type);
        }
        throw new RuntimeException();
    }

    @Transactional(readOnly = true)
    public <T> Stream<T> getAllStreamProjection(Class<T> type) {
        return repository.getAllStreamProjection(type);
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return repository.findByIdProjection(id, type);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerUpdateDTO> getUpdateById(UUID id) {
        return repository.getUpdateById(id);
    }
}
