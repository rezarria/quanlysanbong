package io.rezarria.sanbong.security.service;

import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.repository.RoleRepository;
import io.rezarria.sanbong.service.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
