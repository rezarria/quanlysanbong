package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.interfaces.CustomRepository;
import io.rezarria.sanbong.model.RegisterTemplate;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RegisterTemplateRepository extends CustomRepository<RegisterTemplate, UUID> {
    @Query("select r from RegisterTemplate r order by r.lastModifiedDate desc limit 1")
    Optional<RegisterTemplate> getNewest();
}
