package io.rezarria.sanbong.service;

import io.rezarria.sanbong.interfaces.IService;
import io.rezarria.sanbong.model.ConsumerProduct;
import io.rezarria.sanbong.repository.ConsumerProductRepository;
import io.rezarria.sanbong.security.component.Auth;
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
