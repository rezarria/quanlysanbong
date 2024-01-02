package io.rezarria.service;

import io.rezarria.model.Request;
import io.rezarria.repository.RequestRepository;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;

@RequiredArgsConstructor
public class RequestService extends IService<RequestRepository, Request> {
    @Lazy
    private final RequestRepository requestRepository;
    @Lazy
    private final EntityManager entityManager;

    @Override
    public RequestRepository getRepo() {
        return requestRepository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return requestRepository.findByIdProjection(id, type);
    }

}
