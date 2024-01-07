package io.rezarria.repository;

import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepository extends JpaRepository<AccountRole, AccountRoleKey> {
}
