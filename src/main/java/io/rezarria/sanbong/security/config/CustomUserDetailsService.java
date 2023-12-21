package io.rezarria.sanbong.security.config;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.repository.AccountRepository;
import io.rezarria.sanbong.repository.RoleRepository;
import io.rezarria.sanbong.security.AccountIdInfoAuthority;
import io.rezarria.sanbong.security.UserIdInfoAuthority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.UUID;

@Configuration
public class CustomUserDetailsService {

    @Bean
    UserDetailsService customUserDetails(CustomUserDetails userDetails) {
        return userDetails;
    }

    @Bean
    public CustomUserDetails userDetailsService(AccountRepository accountRepository, RoleRepository roleRepository) {
        return new CustomUserDetails() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                Account account = accountRepository.findByUsername(username).orElseThrow();
                return loadUserByAccount(account);
            }

            private UserDetails loadUserByAccount(Account account) {
                ArrayList<GrantedAuthority> claims = new ArrayList<>();
                claims.add(new AccountIdInfoAuthority(account.getId()));
                var user = account.getUser();
                if (user != null) {
                    claims.add(new UserIdInfoAuthority(user.getId()));
                }
                claims.addAll(
                        account.getRoles().stream().map(AccountRole::getRole)
                                .map(i -> "ROLE_" + i.getName().toUpperCase())
                                .map(SimpleGrantedAuthority::new)
                                .toList());
                return User
                        .withUsername(account.getUsername())
                        .password(account.getPassword())
                        .accountExpired(!account.isActive())
                        .credentialsExpired(!account.isActive())
                        .disabled(!account.isActive())
                        .accountLocked(!account.isActive())
                        .authorities(claims)
                        .build();
            }

            @Override
            public UserDetails loadUserByAccountId(UUID accountId) {
                Account account = accountRepository.findById(accountId).orElseThrow();
                return loadUserByAccount(account);
            }

        };
    }

    public interface CustomUserDetails extends UserDetailsService {
        public UserDetails loadUserByAccountId(UUID accountId);
    }
}
