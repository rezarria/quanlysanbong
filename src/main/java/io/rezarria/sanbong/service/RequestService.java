package io.rezarria.sanbong.service;

import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.sanbong.model.Request;
import io.rezarria.sanbong.repository.RequestRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestService implements IService<Request> {
    @Lazy
    private final RequestRepository requestRepository;
    @Lazy
    private final EntityManager entityManager;

    @Override
    public JpaRepository<Request, UUID> getRepo() {
        return requestRepository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
