package io.rezarria.sanbong.helper;

import io.rezarria.sanbong.security.AccountIdInfoAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class Auth {
    public static Optional<UUID> getAccountId() {
        return getAuthorities().stream().filter(AccountIdInfoAuthority::check).map(AccountIdInfoAuthority::new).findFirst().map(AccountIdInfoAuthority::getValue);
    }

    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthentication().getAuthorities();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean isLogin() {
        return getAuthentication().isAuthenticated();
    }
}
