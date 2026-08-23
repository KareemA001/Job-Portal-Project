package com.spring.job_portal_backend.security;

import com.spring.job_portal_backend.entity.JobPortalUser;
import com.spring.job_portal_backend.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class CustomNonProdUsernamePwdAuthAProvider implements AuthenticationProvider {

    private final JobPortalUserRepository jobPortalUserRepository;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        JobPortalUser returnedUser = jobPortalUserRepository.findUserByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("No user with username "+ username));

        var authenticationResult = new UsernamePasswordAuthenticationToken(returnedUser, null,
                List.of(new SimpleGrantedAuthority(returnedUser.getRole().getName())));
        return authenticationResult;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
