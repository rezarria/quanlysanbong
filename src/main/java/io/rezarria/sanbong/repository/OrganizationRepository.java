package io.rezarria.sanbong.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.sanbong.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

}
