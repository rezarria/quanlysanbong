package io.rezarria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;

public interface AccountRoleRepository extends JpaRepository<AccountRole, AccountRoleKey> {
}
