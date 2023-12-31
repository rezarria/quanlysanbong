package io.rezarria.service;

import io.rezarria.model.User;
import io.rezarria.repository.UserRepository;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService extends IService<UserRepository, User> {
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public UserRepository getRepo() {
        return userRepository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public <T> Page<T> getPage(Pageable page, Class<T> type) {
        return userRepository.getPage(page, type);
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return userRepository.findByIdProjection(id, type);
    }
}
