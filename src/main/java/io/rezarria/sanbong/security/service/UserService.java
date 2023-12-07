package io.rezarria.sanbong.security.service;

import org.springframework.stereotype.Service;

import io.rezarria.sanbong.model.User;
import io.rezarria.sanbong.repository.UserRepository;
import io.rezarria.sanbong.service.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IService<UserRepository, User> {
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
}
