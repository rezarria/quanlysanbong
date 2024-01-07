package io.rezarria.repository;

import io.rezarria.model.RegisterTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RegisterTemplateRepository extends JpaRepository<RegisterTemplate, UUID> {
    @Query("select r from RegisterTemplate r order by r.lastModifiedDate desc limit 1")
    Optional<RegisterTemplate> getNewest();
}
