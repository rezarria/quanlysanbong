package io.rezarria.service;

import io.rezarria.model.ConsumerProduct;
import io.rezarria.repository.ConsumerProductRepository;
import io.rezarria.repository.OrganizationRepository;
import io.rezarria.security.component.Auth;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ConsumerProductService extends IService<ConsumerProductRepository, ConsumerProduct> {

    @Lazy
    private final ConsumerProductRepository repository;

    private final OrganizationRepository organizationRepository;

    @Lazy
    private final EntityManager entityManager;

    @Override
    public ConsumerProductRepository getRepo() {
        return repository;
    }

    @Transactional(readOnly = true)
    public <T> Stream<T> getStream(Class<T> type) {
        Auth auth = new Auth();
        if (auth.hasRole("SUPER_ADMIN"))
            return repository.findAllStream(type);
        return repository.findByOrganization_Accounts_Id(auth.getAccountId(), type);
    }

    @Transactional(readOnly = true)
    public <T> Stream<T> getStreamByName(String name, Class<T> type) {
        Auth auth = new Auth();
        if (auth.hasRole("SUPER_ADMIN")) {
            return repository.findAllByNameContaining(name, type);
        }
        var organizationId = organizationRepository.getIdByAccountId(auth.getAccountId());
        if (organizationId.isPresent()) {
            return repository.findByNameContainsAndCreatedBy_ActiveTrueAndOrganization_Id(name, auth.getAccountId(),
                    type);
        }
        return repository.findByOrganization_Accounts_Id(auth.getAccountId(), type);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public <T> Page<T> getPage(Pageable page, Class<T> type) {
        return repository.getPage(page, type);
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return repository.findByIdProject(id, type);
    }

}
