package com.spring.job_portal_backend.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.job_portal_backend.dto.*;
import com.spring.job_portal_backend.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "resume", required = false) MultipartFile resume,
            Authentication authentication
    ) throws JsonProcessingException {

        String userEmail = authentication.getName();
        ProfileDto profileDto = userService.createOrUpdateUserProfile(userEmail, profileJson, profilePicture, resume);
        return ResponseEntity.ok().body(profileDto);
    }

    @GetMapping(path="/profile/jobseeker", version = "1.0")
    public ResponseEntity<ProfileDto> getProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        ProfileDto profileDto = userService.getProfile(userEmail);
        return ResponseEntity.ok().body(profileDto);
    }

    @GetMapping(path="/profile/picture/jobseeker", version = "1.0")
    public ResponseEntity<byte[]> getProfilePicture(Authentication authentication) {
        String userEmail = authentication.getName();
        ProfileDto profileDto = userService.getProfilePicture(userEmail);
        byte[] picture = profileDto.profilePicture();
        if (picture == null || picture.length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.profilePictureType()));
        headers.setContentLength(picture.length);
        return new ResponseEntity(picture, headers, HttpStatus.OK);
    }

    @GetMapping(path="/profile/resume/jobseeker", version = "1.0")
    public ResponseEntity<byte[]> getProfileResume(Authentication authentication) {
        String userEmail = authentication.getName();
        ProfileDto profileDto = userService.getProfileResume(userEmail);
        byte[] resume = profileDto.resume();
        if (resume == null || resume.length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.resumeType()));
        headers.setContentLength(resume.length);
        headers.setContentDispositionFormData("attachment", profileDto.resumeName());
        return new ResponseEntity(resume, headers, HttpStatus.OK);
    }

    @PostMapping(path="/save-job/{jobId}/jobseeker", version = "1.0")
    public ResponseEntity<JobDto> saveJob(@PathVariable(name = "jobId") Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        JobDto savedJob = userService.saveJob(userEmail, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
    }

    @DeleteMapping(path="/unsave-job/{jobId}/jobseeker", version = "1.0")
    public ResponseEntity<String> unsaveJob(@PathVariable(name = "jobId") Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        userService.unsaveJob(userEmail, id);
        return ResponseEntity.ok("Job is unsaved successfully");
    }

    @GetMapping(path="/savedJobs/jobseeker", version = "1.0")
    public ResponseEntity<List<JobDto>> getAllSavedJobs(Authentication authentication) {
        String userEmail = authentication.getName();
        List<JobDto> savedJobs = userService.getAllSavedJobs(userEmail);
        return ResponseEntity.ok(savedJobs);
    }

    @PostMapping(path="/job-application/jobseeker", version = "1.0")
    public ResponseEntity<JobApplicationDto> applyForJob(
            @RequestBody @Valid ApplyJobRequestDto applyJobRequestDto,
            Authentication authentication) {

        String userEmail = authentication.getName();
        JobApplicationDto jobApplicationDto = userService.applayForJob(userEmail, applyJobRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobApplicationDto);
    }
}
