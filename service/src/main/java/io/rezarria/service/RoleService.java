package io.rezarria.service;

import io.rezarria.model.Role;
import io.rezarria.repository.RoleRepository;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoleService extends IService<RoleRepository, Role> {
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

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return roleRepository.findByIdProjection(id, type);
    }
}
