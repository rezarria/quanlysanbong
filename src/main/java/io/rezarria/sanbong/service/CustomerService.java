package io.rezarria.sanbong.service;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.rezarria.sanbong.model.Customer;
import io.rezarria.sanbong.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements IService<CustomerRepository, Customer> {
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
