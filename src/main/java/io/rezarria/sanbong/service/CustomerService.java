package io.rezarria.sanbong.service;

import io.rezarria.sanbong.model.Customer;
import io.rezarria.sanbong.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
