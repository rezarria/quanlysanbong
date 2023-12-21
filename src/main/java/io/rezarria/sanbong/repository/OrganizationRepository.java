package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.dto.update.organization.OrganizationUpdateDTO;
import io.rezarria.sanbong.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    default Optional<OrganizationUpdateDTO> findByIdForUpdate(UUID id) {
        var account = findById(id);
        return account.map(OrganizationUpdateDTO::create);
    }
}
