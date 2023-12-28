package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.interfaces.CustomRepository;
import io.rezarria.sanbong.model.RegisterTemplateRole;
import io.rezarria.sanbong.model.RegisterTemplateRoleKey;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegisterTemplateRoleRepository extends CustomRepository<RegisterTemplateRole, RegisterTemplateRoleKey> {
    @Query("select r from RegisterTemplateRole  r order by r.lastModifiedDate desc limit 1")
    Optional<RegisterTemplateRole> getNewest();
}
