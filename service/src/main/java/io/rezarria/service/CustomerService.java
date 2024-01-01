package io.rezarria.service;

import io.rezarria.model.Customer;
import io.rezarria.repository.CustomerRepository;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public <T> Stream<T> getAllStreamProjection(Class<T> type) {
        return repository.getAllStreamProjection(type);
    }
}
