package com.spring.job_portal_backend.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.job_portal_backend.dto.ProfileDto;
import com.spring.job_portal_backend.dto.UserDto;
import com.spring.job_portal_backend.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path="/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping(path="/search/admin")
    public ResponseEntity<?> searchUserByEmail(@RequestParam String email) {
        Optional<UserDto> userOptional = userService.searchUserByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with email: " + email));
        }
        return ResponseEntity.ok(userOptional.get());
    }

    @PatchMapping(path="/{userId}/role/employer/admin", version = "1.0")
    public ResponseEntity<?> elevateToEmployer(@PathVariable Long userId) {
        UserDto updatedUser = userService.promoteToEmployer(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(path="/{userId}/company/{companyId}/admin", version = "1.0")
    public ResponseEntity<?> assignCompanyToEmployer(
            @PathVariable Long userId, @PathVariable Long companyId) {
        UserDto updatedUser = userService.assignCompanyToEmployer(userId, companyId);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(path="/profile/jobseeker", version = "1.0", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDto> createOrUpdateUserProfile(
            @RequestPart(value = "profile") String profileJson,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePicture,
            @RequestPart(value = "resume", required = false) MultipartFile resume,
            Authentication authentication
            ) throws JsonProcessingException {

        String userEmail = authentication.getName();
        ProfileDto profileDto = userService.createOrUpdateUserProfile(userEmail, profileJson, profilePicture, resume);
        return ResponseEntity.ok().body(profileDto);
    }
}
