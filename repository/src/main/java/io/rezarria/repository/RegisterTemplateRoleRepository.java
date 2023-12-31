package io.rezarria.repository;

import io.rezarria.model.RegisterTemplateRole;
import io.rezarria.model.RegisterTemplateRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegisterTemplateRoleRepository extends JpaRepository<RegisterTemplateRole, RegisterTemplateRoleKey> {
    @Query("select r from RegisterTemplateRole  r order by r.lastModifiedDate desc limit 1")
    Optional<RegisterTemplateRole> getNewest();
}
