package io.rezarria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.rezarria.model.RegisterTemplateRole;
import io.rezarria.model.RegisterTemplateRoleKey;

public interface RegisterTemplateRoleRepository extends JpaRepository<RegisterTemplateRole, RegisterTemplateRoleKey> {
    @Query("select r from RegisterTemplateRole  r order by r.lastModifiedDate desc limit 1")
    Optional<RegisterTemplateRole> getNewest();
}
