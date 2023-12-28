package io.rezarria.repository;

import io.rezarria.model.FieldUnitSetting;
import io.rezarria.repository.interfaces.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface FieldUnitSettingRepository extends CustomRepository<FieldUnitSetting, UUID> {
    @Query("select f from FieldUnitSetting f inner join f.currentField fields where fields.id = ?1")
    <T>
    Optional<T> findByFields_Id(UUID id, Class<T> type);

}