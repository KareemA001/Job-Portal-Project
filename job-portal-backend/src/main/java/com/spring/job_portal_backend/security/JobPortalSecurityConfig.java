package com.spring.job_portal_backend.security;


import com.spring.job_portal_backend.security.filter.JwtTokenValidationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
        return http.authorizeHttpRequests(requests -> {
                    publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                    securedPaths.forEach(path -> requests.requestMatchers(path).authenticated());
                    requests.anyRequest().denyAll();
                })
                .cors(ccs -> ccs.configurationSource(corsConfigurationSource()))
                .csrf(ccc -> ccc.disable())
                .addFilterBefore(new JwtTokenValidationFilter(publicPaths), BasicAuthenticationFilter.class)
                .formLogin(flc -> flc.disable())
                .httpBasic(hbc -> hbc.disable())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

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
