package io.rezarria.sanbong.security.service;

import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.repository.RoleRepository;
import io.rezarria.sanbong.service.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IService<Role> {
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
    public JpaRepository<Role, UUID> getRepo() {
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
