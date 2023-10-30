package io.rezarria.sanbong.security.config;

import io.rezarria.sanbong.security.jwt.JwtFilter;
import io.rezarria.sanbong.security.jwt.JwtUtils;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider,
                                              JwtUtils jwtUtil) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry ->
                        registry
                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .requestMatchers("/api/security/checkInfo").permitAll()
                                .requestMatchers("/api/**").permitAll()
                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ASYNC, DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.INCLUDE).permitAll()
                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
