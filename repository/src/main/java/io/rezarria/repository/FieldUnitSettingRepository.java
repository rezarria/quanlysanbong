package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.interfaces.CustomRepository;
import io.rezarria.sanbong.model.FieldUnitSetting;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface FieldUnitSettingRepository extends CustomRepository<FieldUnitSetting, UUID> {
    @Query("select f from FieldUnitSetting f inner join f.currentField fields where fields.id = ?1")
    <T>
    Optional<T> findByFields_Id(UUID id, Class<T> type);

}