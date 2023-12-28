package io.rezarria.sanbong.security.auditor_aware;

import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.repository.CustomRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@EnableJpaRepositories(basePackages = "io.rezarria.sanbong.repository", repositoryBaseClass = CustomRepositoryImpl.class)
public class Config {
    @Bean
    public AuditorAware<Account> auditorAware() {
        return new UserAuditorAware();
    }
}
