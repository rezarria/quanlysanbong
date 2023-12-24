package io.rezarria.sanbong.service;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.repository.FieldRepository;
import io.rezarria.sanbong.repository.OrganizationRepository;
import io.rezarria.sanbong.security.component.Auth;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldService implements IService<FieldRepository, Field> {
    private final FieldRepository fieldRepository;
    private final OrganizationRepository organizationRepository;
    private final EntityManager entityManager;

    @Override
    public Field create(Field entity) {
        Auth auth = new Auth();
        if (auth.isLogin()) {
            if (!auth.hasRole("ROLE_ADMIN")) {
                var organization = organizationRepository.findByAccounts_Id(auth.getAccountId()).orElseThrow();
                entity.setOrganization(organization);
            }
        }
        return IService.super.create(entity);
    }

    public <T> Page<T> getPage(Pageable pageable, Class<T> type) {
        Auth auth = new Auth();
        if (auth.hasRole("ROLE_SUPER_ADMIN")) return fieldRepository.findAllCustom(pageable, type);
        return fieldRepository.findByOrganization_Accounts_Id(auth.getAccountId(), pageable, type);
    }

    public <T> Stream<T> getStream(Class<T> type) {
        Auth auth = new Auth();
        if (auth.hasRole("SUPER_ADMIN")) return fieldRepository.findAllStream(type);
        return fieldRepository.findByOrganization_Accounts_Id__Stream(auth.getAccountId(), type);
    }

    @Override
    public FieldRepository getRepo() {
        return fieldRepository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Field> getManyByName(Collection<String> names) {
        return fieldRepository.findAllByNameIn(names);
    }

    public List<Field> getFieldsByOrganizationId(UUID id) {
        return fieldRepository.findAllByOrganizationId(id);
    }

    public void remove(String name) throws IllegalArgumentException {
        fieldRepository.deleteByName(name);
    }

    public void remove(Collection<String> names) {
        fieldRepository.deleteAllByNameIn(names);
    }

    @SneakyThrows
    public <T> List<T> getFieldsByName(String name, Class<T> classType) {
        Auth auth = new Auth();
        if (auth.isLogin()) {
            return null;
        } else {
            throw new PermissionDeniedDataAccessException("field", null);
        }
    }

    public enum Status {FREE, PAYING, BUSY, DENY, UNKNOWN}

    @Builder
    record FieldStatus(UUID id, Status status) {
    }

    public Stream<Status> getStatus(Iterable<UUID> ids) {
        return null;
    }

}
