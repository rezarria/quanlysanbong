package io.rezarria.sanbong.service;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.rezarria.sanbong.model.Field;
import io.rezarria.sanbong.repository.FieldHistoryRepository;
import io.rezarria.sanbong.repository.FieldRepository;
import io.rezarria.sanbong.repository.OrganizationRepository;
import io.rezarria.sanbong.security.component.Auth;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldService implements IService<FieldRepository, Field> {
    private final FieldRepository fieldRepository;
    private final FieldHistoryRepository fieldHistoryRepository;
    private final OrganizationRepository organizationRepository;
    private final EntityManager entityManager;

    @Override
    public Field create(Field entity) {
        Auth auth = new Auth();
        if (auth.isLogin()) {
            if (!auth.hasRole("SUPER_ADMIN")) {
                var organization = organizationRepository.findByAccounts_Id(auth.getAccountId()).orElseThrow();
                entity.setOrganization(organization);
            }
        }
        return IService.super.create(entity);
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

    public enum Status {
        FREE, BUSY, DENY, UNKNOWN
    }

    @Builder
    record FieldStatus(UUID id, Status status) {
    }

}
