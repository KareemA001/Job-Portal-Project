package com.spring.job_portal_backend.auth;

import com.spring.job_portal_backend.constants.ApplicationConstants;
import com.spring.job_portal_backend.dto.LoginRequestDto;
import com.spring.job_portal_backend.dto.LoginResponseDto;
import com.spring.job_portal_backend.dto.RegisterRequestDto;
import com.spring.job_portal_backend.dto.UserDto;
import com.spring.job_portal_backend.entity.JobPortalUser;
import com.spring.job_portal_backend.entity.Role;
import com.spring.job_portal_backend.repository.JobPortalUserRepository;
import com.spring.job_portal_backend.repository.RoleRepository;
import com.spring.job_portal_backend.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path="/auth")
@RequiredArgsConstructor
public class AuthController {

    @Qualifier(value="authenticationManager")
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @PostMapping(path="/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        try {
            var authenticationResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    requestDto.username(), requestDto.password()));
            var userDto = new UserDto();
            var loggedInUser = (JobPortalUser) authenticationResult.getPrincipal();
            BeanUtils.copyProperties(loggedInUser, userDto);
            userDto.setRole(loggedInUser.getRole().getName());
            userDto.setUserId(loggedInUser.getId());
            String jwtToken = jwtUtil.generateJwtToken(authenticationResult);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), userDto, jwtToken));

        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }
    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new LoginResponseDto(message, null, null));
    }

    @PostMapping(path="/register/public", version = "1.0")
    public ResponseEntity<?> login(@RequestBody RegisterRequestDto requestDto) {

        JobPortalUser user = new JobPortalUser();
        BeanUtils.copyProperties(requestDto, user);
        Role role = roleRepository.findRoleByName(ApplicationConstants.ROLE_JOB_SEEKER)
                .orElseThrow(() -> new IllegalArgumentException("Role "+ ApplicationConstants.ROLE_JOB_SEEKER+" not found"));
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(requestDto.password()));
        jobPortalUserRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User is registered successfully");

    }
}
