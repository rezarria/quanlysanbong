package io.rezarria.repository;

import io.rezarria.model.FieldUnitSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface FieldUnitSettingRepository extends JpaRepository<FieldUnitSetting, UUID> {
    @Query("select f from FieldUnitSetting f inner join f.currentField fields where fields.id = ?1")
    <T> Optional<T> findByFields_Id(UUID id, Class<T> type);

    @Query("select f from FieldUnitSetting f where f.id = ?1")
    <T> Optional<T> findByIdProjection(UUID id, Class<T> type);

}