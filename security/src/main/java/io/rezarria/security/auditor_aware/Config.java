package io.rezarria.security.auditor_aware;

import io.rezarria.model.Account;
import io.rezarria.repository.CustomRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
public class Config {
    @Bean
    public AuditorAware<Account> auditorAware() {
        return new UserAuditorAware();
    }
}
