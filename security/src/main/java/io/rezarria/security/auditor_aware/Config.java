package io.rezarria.security.auditor_aware;

import io.rezarria.model.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class Config {
    @Bean
    public AuditorAware<Account> auditorAware() {
        return new UserAuditorAware();
    }
}
