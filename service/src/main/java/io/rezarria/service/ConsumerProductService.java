package io.rezarria.service;

import io.rezarria.model.ConsumerProduct;
import io.rezarria.repository.ConsumerProductRepository;
import io.rezarria.security.component.Auth;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

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

    public <T> Stream<T> getStream(Class<T> type) {
        Auth auth = new Auth();
        if (auth.hasRole("SUPER_ADMIN"))
            return repository.findAllStream(type);
        return repository.findByOrganization_Accounts_Id(auth.getAccountId(), type);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
