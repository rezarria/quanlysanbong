package io.rezarria.sanbong.security.component;

import io.rezarria.sanbong.security.AccountIdInfoAuthority;
import io.rezarria.sanbong.security.UserIdInfoAuthority;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Scope("prototype")
public class Auth {
    private Authentication authentication;
    @Getter
    private UUID accountId;
    @Getter
    private UUID userId;

    private Collection<? extends GrantedAuthority> claims;

    private List<String> roles;

    @PostConstruct
    public void init() {
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            claims = authentication.getAuthorities();
            var account = claims.stream().filter(AccountIdInfoAuthority::check).map(AccountIdInfoAuthority::new)
                    .findFirst();
            accountId = account.isPresent() ? account.get().getValue() : null;
            var user = claims.stream().filter(UserIdInfoAuthority::check).map(UserIdInfoAuthority::new)
                    .findFirst();
            userId = user.isPresent() ? user.get().getValue() : null;
            roles = claims.stream().filter(i -> i.getAuthority().startsWith("ROLE_"))
                    .map(i -> {
                        String str = i.getAuthority();
                        return str.substring(4);
                    })
                    .toList();
        }
    }

    public Optional<String> get(String name) {
        return claims.stream().filter(c -> c.getAuthority().startsWith(name)).map(GrantedAuthority::getAuthority)
                .findFirst();
    }

    public boolean hasRole(String role) {
        return this.roles.contains(role);
    }

    public boolean isLogin() {
        return authentication.isAuthenticated();
    }
}
