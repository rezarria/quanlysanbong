package io.rezarria.sanbong.service;

import org.springframework.stereotype.Service;

import io.rezarria.sanbong.model.Organization;
import io.rezarria.sanbong.repository.OrganizationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}