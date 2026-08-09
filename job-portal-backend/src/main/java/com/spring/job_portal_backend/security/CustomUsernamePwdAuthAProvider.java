package com.spring.job_portal_backend.security;

import com.spring.job_portal_backend.entity.JobPortalUser;
import com.spring.job_portal_backend.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUsernamePwdAuthAProvider implements AuthenticationProvider {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        JobPortalUser returnedUser = jobPortalUserRepository.findUserByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("No user with username "+ username));

        if (passwordEncoder.matches(password, returnedUser.getPasswordHash())) {

            var authenticationResult = new UsernamePasswordAuthenticationToken(returnedUser, null,
                    List.of(new SimpleGrantedAuthority(returnedUser.getRole().getName())));
            return authenticationResult;

        } else {
                throw new BadCredentialsException("Invalid credentials");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
