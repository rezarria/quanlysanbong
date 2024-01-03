package io.rezarria.service;

import io.rezarria.model.Field;
import io.rezarria.model.FieldHistory;
import io.rezarria.repository.FieldHistoryRepository;
import io.rezarria.repository.FieldRepository;
import io.rezarria.repository.OrganizationRepository;
import io.rezarria.security.component.Auth;
import io.rezarria.service.exceptions.FieldOrderServiceException;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldService extends IService<FieldRepository, Field> {
    @Lazy
    private final FieldRepository fieldRepository;
    @Lazy
    private final FieldHistoryRepository fieldHistoryRepository;
    @Lazy
    private final OrganizationRepository organizationRepository;
    @Lazy
    private final EntityManager entityManager;

    public void order(FieldHistory order) throws FieldOrderServiceException {
        var count = fieldHistoryRepository.countByField_IdAndFromLessThanEqual(order.getField().getId(),
                order.getFrom());
        if (count != 0)
            throw FieldOrderServiceException.NotFit(order);

    }

    @Override
    public Field create(Field entity) {
        Auth auth = new Auth();
        if (auth.isLogin()) {
            if (!auth.hasRole("SUPER_ADMIN")) {
                var organization = organizationRepository.findByAccounts_Id(auth.getAccountId()).orElseThrow();
                entity.setOrganization(organization);
            }
        }
        return super.create(entity);
    }

    public <T> Page<T> getPage(Pageable pageable, Class<T> type) {
        Auth auth = new Auth();
        if (auth.hasRole("SUPER_ADMIN"))
            return fieldRepository.findAllCustom(pageable, type);
        return fieldRepository.findByOrganization_Accounts_Id(auth.getAccountId(), pageable, type);
    }

    public <T> Page<T> getPublicPage(Pageable pageable, Class<T> type) {
        return fieldRepository.getFields(pageable, type);
    }

    public <T> Stream<T> getStream(Class<T> type) {
        Auth auth = new Auth();
        if (auth.hasRole("SUPER_ADMIN"))
            return fieldRepository.findAllStream(type);
        return fieldRepository.findByOrganization_Accounts_Id__Stream(auth.getAccountId(), type);
    }

    public <T> Optional<T> findByIdProjection(UUID id, Class<T> type) {
        return fieldRepository.findByIdProject(id, type);
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

    public Stream<Status> getStatus(Iterable<UUID> ids) {
        return null;
    }

    public Status getStatus(UUID id) {
        try {
            var field = fieldRepository.findById(id).orElseThrow();
            if (!field.isActive())
                return Status.DENY;
            var setting = field.getCurrentUnitSetting();
            var openTime = setting.getOpenTime();
            var closeTime = setting.getCloseTime();
            var start = Calendar.getInstance(TimeZone.getDefault());
            start.set(Calendar.HOUR_OF_DAY, openTime / 60);
            start.set(Calendar.MINUTE, openTime % 60);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);
            var end = (Calendar) start.clone();
            end.set(Calendar.MINUTE, closeTime % 60);
            end.set(Calendar.HOUR_OF_DAY, openTime / 60);
            var list = fieldHistoryRepository.findByField_IdAndFromLessThanEqualAndToGreaterThanEqual(id,
                    start.toInstant(), start.toInstant());
            var time = list.stream().map(i -> {
                if (i.getUnitSetting().isUnitStyle()) {
                    return i.getUnitSize() * (long) i.getUnitSetting().getDuration();
                } else {
                    return ChronoUnit.MINUTES.between(i.getFrom(), i.getTo());
                }
            }).mapToLong(Long::valueOf).sum();
            return (closeTime - openTime - time <= setting.getDuration()) ? Status.BUSY : Status.FREE;
        } catch (Exception e) {
            return Status.UNKNOWN;
        }
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return fieldRepository.findByIdProject(id, type);
    }

    public enum Status {
        FREE, BUSY, DENY, UNKNOWN
    }

    @Builder
    record FieldStatus(UUID id, Status status) {
    }

}
