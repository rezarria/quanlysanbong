package io.rezarria.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

/**
 * Projection for {@link io.rezarria.model.Account}
 */
public interface AccountInfo {
    UUID getId();

    String getUsername();

    boolean isActive();

    @Value("#{target.user != null ? target.user.id : null}")
    UUID getUserId();

    List<AccountRoleInfo> getRoles();

    /**
     * Projection for {@link io.rezarria.model.AccountRole}
     */
    interface AccountRoleInfo {
        RoleInfo getRole();

        /**
         * Projection for {@link io.rezarria.model.Role}
         */
        interface RoleInfo {
            UUID getId();

            String getName();

            String getDisplayName();
        }
    }
}