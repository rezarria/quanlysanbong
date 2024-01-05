package io.rezarria.service;

import io.rezarria.model.FieldHistory;
import io.rezarria.repository.*;
import io.rezarria.security.component.Auth;
import io.rezarria.service.exceptions.FieldOrderServiceException;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FieldHistoryService extends IService<FieldHistoryRepository, FieldHistory> {
    @Lazy
    private final FieldHistoryRepository repository;
    @Lazy
    private final FieldUnitSettingRepository fieldUnitSettingRepository;
    @Lazy
    private final EntityManager entityManager;
    @Lazy
    private final CustomerRepository customerRepository;
    @Lazy
    private final FieldRepository fieldRepository;
    @Lazy
    private final StaffRepository staffRepository;

    @Override
    public FieldHistoryRepository getRepo() {
        return repository;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional(readOnly = true)
    public boolean isFree(UUID id) {
        var now = Instant.now();
        return repository.countByField_IdAndFromLessThanEqualAndToGreaterThanEqual(id, now, now) == 0L;
    }

    @Transactional(readOnly = true)
    public List<FieldHistory> getSchedule(UUID id) {
        var setting = fieldUnitSettingRepository.findByFields_Id(id, SettingProjection.class).orElseThrow();
        var openTime = setting.getOpenTime();
        var closeTime = setting.getCloseTime();
        var start = Calendar.getInstance(TimeZone.getDefault());
        start.set(Calendar.HOUR_OF_DAY, openTime / 60);
        start.set(Calendar.MINUTE, openTime % 60);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        var end = (Calendar) start.clone();
        end.set(Calendar.MINUTE, closeTime % 60);
        end.set(Calendar.HOUR_OF_DAY, closeTime / 60);
        return repository.findByField_IdAndFromLessThanEqualAndToGreaterThanEqual(id,
                start.toInstant(), end.toInstant());
    }

    private Instant getStart(UUID id) {
        var setting = fieldUnitSettingRepository.findByFields_Id(id, SettingProjection.class).orElseThrow();
        var openTime = setting.getOpenTime();
        var start = Calendar.getInstance(TimeZone.getDefault());
        start.set(Calendar.HOUR_OF_DAY, openTime / 60);
        start.set(Calendar.MINUTE, openTime % 60);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.toInstant();
    }

    @Transactional(readOnly = true)
    public <T> Page<T> getSchedule(UUID id, Pageable page, Class<T> type) {
        return repository.getSchedule(id, getStart(id), page, type);
    }

    @Transactional(readOnly = true)
    public <T> Stream<T> getSchedule(UUID id, Class<T> type) {
        var setting = fieldUnitSettingRepository.findByFields_Id(id, SettingProjection.class).orElseThrow();
        var openTime = setting.getOpenTime();
        var start = Calendar.getInstance(TimeZone.getDefault());
        start.set(Calendar.HOUR_OF_DAY, openTime / 60);
        start.set(Calendar.MINUTE, openTime % 60);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return repository.getSchedule(id, getStart(id), type);
    }

    @Override
    public <A> Optional<A> getByIdProjection(UUID id, Class<A> type) {
        return fieldUnitSettingRepository.findByIdProjection(id, type);
    }

    public FieldHistory order(UUID customerId, UUID fieldId, Instant from, Instant to) throws FieldOrderServiceException {
        var count = repository.countByField_IdAndFromLessThanEqualAndToGreaterThanEqual(fieldId, from, to);
        if (count == 0) {
            var builder = FieldHistory.builder()
                    .customer(customerRepository.findById(customerId).orElseThrow())
                    .from(from).to(to)
                    .field(fieldRepository.findById(fieldId).orElseThrow());
            var auth = new Auth();
            if (auth.isLogin() && !auth.hasRole("SUPER_ADMIN")) {
                var staff = staffRepository.findByAccount_Id(auth.getAccountId()).orElseThrow();
                builder.staff(staff);
            }
            return repository.save(builder.build());
        }
        return null;
    }

    public interface SettingProjection {
        int getOpenTime();

        int getCloseTime();
    }

    public interface FieldHistoryProjection {
        Instant getFrom();

        Instant getTo();
    }

}
