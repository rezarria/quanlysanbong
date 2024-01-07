package io.rezarria.spring;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan("io.rezarria.model")
@ComponentScan(basePackages = "io.rezarria.*")
@ConfigurationPropertiesScan(basePackages = "io.rezarria.*")
@EnableJpaRepositories(basePackages = "io.rezarria.repository")
@EnableScheduling
@EnableWebSecurity
@EnableJpaAuditing
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@EnableTransactionManagement
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SanbongApplication {

    public static void main(String[] args) {
        SpringApplication.run(SanbongApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
