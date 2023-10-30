package io.rezarria.sanbong.security.auditorAware;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.security.Details;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UserAuditorAware implements AuditorAware<Account> {
    @Override
    @NonNull
    public Optional<Account> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getDetails)
                .map(Details.class::cast)
                .map(Details::getAccountId)
                .map(id -> Account.builder().id(id).build());
    }
}
