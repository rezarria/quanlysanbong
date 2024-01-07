package io.rezarria.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.rezarria.model.RegisterTemplate;

public interface RegisterTemplateRepository extends JpaRepository<RegisterTemplate, UUID> {
    @Query("select r from RegisterTemplate r order by r.lastModifiedDate desc limit 1")
    Optional<RegisterTemplate> getNewest();
}
