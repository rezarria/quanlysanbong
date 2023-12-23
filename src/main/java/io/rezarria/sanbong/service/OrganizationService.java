package io.rezarria.sanbong.service;

import io.rezarria.sanbong.model.Organization;
import io.rezarria.sanbong.repository.OrganizationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationService implements IService<OrganizationRepository, Organization> {

    private final OrganizationRepository repository;
    private final EntityManager entityManager;

    @Override
    public OrganizationRepository getRepo() {
        return repository;
    }

    public Optional<Organization> findByAccountId(UUID id) {
        return repository.findByAccounts_Id(id);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
