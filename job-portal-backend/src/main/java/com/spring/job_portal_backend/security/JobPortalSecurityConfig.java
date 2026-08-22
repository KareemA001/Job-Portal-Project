package com.spring.job_portal_backend.security;


import com.spring.job_portal_backend.security.filter.JwtTokenValidationFilter;
import com.spring.job_portal_backend.utility.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JobPortalSecurityConfig {

    @Qualifier(value="publicPaths")
    private final List<String> publicPaths;
    @Qualifier(value="securedPaths")
    private final List<String> securedPaths;
    @Qualifier(value="adminPaths")
    private final List<String> adminPaths;
    @Qualifier(value="employerPaths")
    private final List<String> employerPaths;
    @Qualifier(value = "jobseekerPaths")
    private final List<String> jobseekerPaths;

    private final CorsProperties corsProperties;
    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
        return http.authorizeHttpRequests(requests -> {
                    publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                    adminPaths.forEach(path -> requests.requestMatchers(path).hasRole("ADMIN"));
                    employerPaths.forEach(path -> requests.requestMatchers(path).hasRole("EMPLOYER"));
                    jobseekerPaths.forEach(path -> requests.requestMatchers(path).hasRole("JOB_SEEKER"));
                    securedPaths.forEach(path -> requests.requestMatchers(path).authenticated());
                    requests.anyRequest().denyAll();
                })
                .cors(ccs -> ccs.configurationSource(corsConfigurationSource()))
                .csrf(csrfConfig -> csrfConfig
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))

                .addFilterBefore(new JwtTokenValidationFilter(publicPaths), BasicAuthenticationFilter.class)
                .formLogin(flc -> flc.disable())
                .httpBasic(hbc -> hbc.disable())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(corsProperties.getAllowedOrigins());
        config.setAllowedMethods(corsProperties.getAllowedMethods());
        config.setAllowedHeaders(corsProperties.getAllowedHeaders());
        config.setAllowCredentials(corsProperties.getAllowedCredentials());
        config.setMaxAge(corsProperties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
////        String hashedValue_1 = passwordEncoder().encode("Karim0000");
////        String hashedValue_2 = passwordEncoder().encode("Karim_0000");
////        System.out.println(hashedValue_1);
////        System.out.println(hashedValue_2);
//        User user_1 = new User("Karim","{bcrypt}$2a$10$vya8S1n8YlUf7ahX7Yfek.0J5vO.tzDFaVhJY6QTiaZ6yaU/0x022",
//                List.of(new SimpleGrantedAuthority("USER")));
//        User user_2 = new User("Alaa","{bcrypt}$2a$10$4NEcYnwM6O1bMfX27FzfGeaFWb/Tm4hx8NNk0/xg0/9PQ41OP2EIK",
//                List.of(new SimpleGrantedAuthority("ADMIN")));
//        return new InMemoryUserDetailsManager(user_1,user_2);
//    }

    @Bean(name="authenticationManager")
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
//        var authenticationProvider = new DaoAuthenticationProvider(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
        var providerManager = new ProviderManager(authenticationProvider);
        return providerManager;
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
