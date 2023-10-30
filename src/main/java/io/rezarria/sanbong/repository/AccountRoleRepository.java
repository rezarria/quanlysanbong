package io.rezarria.sanbong.repository;

import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.model.AccountRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepository extends JpaRepository<AccountRole, AccountRoleKey> {
}
