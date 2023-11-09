package io.rezarria.sanbong.security.service;

import io.rezarria.sanbong.model.Role;
import io.rezarria.sanbong.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoleService {
    @Lazy
    private final RoleRepository roleRepository;
    @Lazy
    private final EntityManager entityManager;

    @Transactional
    public Stream<Role> getAll() {
        return roleRepository.findAll().stream();
    }

    public Role getById(UUID id) {
        return roleRepository.getReferenceById(id);
    }

    public Role add(String name) {
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    public void deleteAll(Collection<UUID> ids) {
        var list = roleRepository.findAllById(ids);
        roleRepository.deleteAll(roleRepository.saveAll(list));
    }

    @Transactional
    public Role update(Role role) {
        return entityManager.merge(role);
    }

}
