package io.rezarria.sanbong.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.sanbong.dto.update.organization.OrganizationUpdateDTO;
import io.rezarria.sanbong.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    default Optional<OrganizationUpdateDTO> findByIdForUpdate(UUID id) {
        var account = findById(id);
        if (account.isEmpty())
            return Optional.empty();
        return Optional.of(OrganizationUpdateDTO.create(account.get()));
    }
}
