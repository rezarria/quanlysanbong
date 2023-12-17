package io.rezarria.sanbong.security.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.AccountRole;
import io.rezarria.sanbong.repository.AccountRepository;
import io.rezarria.sanbong.repository.RoleRepository;

@Configuration
public class CustomUserDetailsService {

    public interface CustomUserDetails extends UserDetailsService {
        public UserDetails loadUserByAccountId(UUID accountId);
    }

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
                return User
                        .withUsername(account.getUsername())
                        .password(account.getPassword())
                        .roles(account.getRoles().stream().map(AccountRole::getRole)
                                .map(i -> "ROLE_" + i.getName().toUpperCase()).toList()
                                .toArray(new String[0]))
                        .accountExpired(!account.isActive())
                        .credentialsExpired(!account.isActive())
                        .disabled(!account.isActive())
                        .accountLocked(!account.isActive())
                        .build();
            }

            @Override
            public UserDetails loadUserByAccountId(UUID accountId) {
                Account account = accountRepository.findById(accountId).orElseThrow();
                return User
                        .withUsername(account.getUsername())
                        .password(account.getPassword())
                        .roles(account.getRoles().stream().map(AccountRole::getRole)
                                .map(i -> "ROLE_" + i.getName().toUpperCase()).toList()
                                .toArray(new String[0]))
                        .accountExpired(!account.isActive())
                        .credentialsExpired(!account.isActive())
                        .disabled(!account.isActive())
                        .accountLocked(!account.isActive())
                        .build();
            }

        };
    }
}
