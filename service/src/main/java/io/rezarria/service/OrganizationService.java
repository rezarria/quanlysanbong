package io.rezarria.service;

import io.rezarria.model.Organization;
import io.rezarria.repository.OrganizationRepository;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationService extends IService<OrganizationRepository, Organization> {

    private final OrganizationRepository repository;
    private final EntityManager entityManager;

    @Override
    public OrganizationRepository getRepo() {
        return repository;
    }

    public <T> Optional<T> findByIdProjection(UUID id, Class<T> type) {
        return repository.findByIdProjection(id, type);
    }

    public Optional<Organization> findByAccountId(UUID id) {
        return repository.findByAccounts_Id(id);
    }

    public <T> Page<T> findByIds(Collection<UUID> ids, Pageable page, Class<T> type) {
        return repository.findByIdIn(ids, page, type);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
