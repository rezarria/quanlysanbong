package io.rezarria.sanbong.security.service;

import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.repository.RoleRepository;
import io.rezarria.sanbong.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoleService implements IService<RoleRepository, Role> {
    @Lazy
    private final RoleRepository roleRepository;
    @Lazy
    private final EntityManager entityManager;

    public Role add(String name) {
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    @Override
    public RoleRepository getRepo() {
        return roleRepository;
    }

    public <T> Stream<T> findAllByName(String name) {
        return roleRepository.findAllByNameContaining(name);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
