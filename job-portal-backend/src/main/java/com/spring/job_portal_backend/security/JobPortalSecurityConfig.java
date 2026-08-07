package com.spring.job_portal_backend.security;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class JobPortalSecurityConfig {

    @Qualifier(value="publicPaths")
    private final List<String> publicPaths;
    @Qualifier(value="securedPaths")
    private final List<String> securedPaths;

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
        return http.authorizeHttpRequests(requests -> {
                    publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                    securedPaths.forEach(path -> requests.requestMatchers(path).authenticated());
                    requests.anyRequest().denyAll();
                })
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .build();
    }
}
