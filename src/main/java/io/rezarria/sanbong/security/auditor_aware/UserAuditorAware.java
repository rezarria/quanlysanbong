package io.rezarria.sanbong.security.auditor_aware;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.security.AccountIdInfoAuthority;

public class UserAuditorAware implements AuditorAware<Account> {
    @Override
    @NonNull
    public Optional<Account> getCurrentAuditor() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var accountId = authentication.getAuthorities()
                    .stream()
                    .filter(AccountIdInfoAuthority::check)
                    .map(AccountIdInfoAuthority::new).findFirst().orElseThrow().getValue();
            return Optional.of(Account.builder().id(accountId).build());
        }

        return Optional.empty();
    }
}
