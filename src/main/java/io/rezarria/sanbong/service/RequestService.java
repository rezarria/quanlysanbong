package io.rezarria.sanbong.service;

import org.springframework.context.annotation.Lazy;

import io.rezarria.sanbong.model.Request;
import io.rezarria.sanbong.repository.RequestRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestService implements IService<RequestRepository, Request> {
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

}
