package io.rezarria.sanbong.service;

import io.rezarria.sanbong.model.ConsumerProduct;
import io.rezarria.sanbong.repository.ConsumerProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ConsumerProductService implements IService<ConsumerProductRepository, ConsumerProduct> {

    @Lazy
    private final ConsumerProductRepository repository;
    @Lazy
    private final EntityManager entityManager;

    @Override
    public ConsumerProductRepository getRepo() {
        return repository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
