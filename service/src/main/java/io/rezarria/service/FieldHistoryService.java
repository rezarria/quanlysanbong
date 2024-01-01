package io.rezarria.service;

import io.rezarria.model.FieldHistory;
import io.rezarria.repository.FieldHistoryRepository;
import io.rezarria.repository.FieldUnitSettingRepository;
import io.rezarria.service.interfaces.IService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FieldHistoryService extends IService<FieldHistoryRepository, FieldHistory> {
    private final FieldHistoryRepository repository;
    private final FieldUnitSettingRepository fieldUnitSettingRepository;
    private final EntityManager entityManager;

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

    public interface SettingProjection {
        int getOpenTime();

        int getCloseTime();
    }

    public interface FieldHistoryProjection {
        Instant getFrom();

        Instant getTo();
    }

}
