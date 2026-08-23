package com.spring.job_portal_backend.job.controller;

import com.spring.job_portal_backend.dto.JobApplicationDto;
import com.spring.job_portal_backend.dto.JobDto;
import com.spring.job_portal_backend.dto.UpdateJobApplicationDto;
import com.spring.job_portal_backend.job.service.IJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/jobs")
@RequiredArgsConstructor
public class JobController {

    private final IJobService jobService;

    @GetMapping(path = "/employer", version = "1.0")
    public ResponseEntity<List<JobDto>> getEmployerJobs(Authentication authentication) {

        String employerEmail = authentication.getName();
        List<JobDto> jobs = jobService.getEmployerJobs(employerEmail);
        return ResponseEntity.ok(jobs);
    }

    @PatchMapping(path="/{jobId}/status/employer")
    public ResponseEntity<?> updateJobStatus(@PathVariable(name = "jobId") Long jobId,
                                             @RequestBody Map<String, String> requestBody,
                                             Authentication authentication) {

        String employerEmail = authentication.getName();
        String jobStatus = requestBody.get("status");

        if (jobStatus == null || jobStatus.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Status is required"));
        }
        JobDto updatedJob = jobService.updateJobStatus(jobId, jobStatus.toUpperCase(), employerEmail);
        return ResponseEntity.ok(updatedJob);
    }

    @PostMapping(path = "/employer", version = "1.0")
    public ResponseEntity<JobDto> createJob(@RequestBody @Valid JobDto jobDto, Authentication authentication) {

        String employerEmail = authentication.getName();
        JobDto createdJob = jobService.createJob(jobDto, employerEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @GetMapping(path= "/applications/{jobId}/employer", version = "1.0")
    public ResponseEntity<List<JobApplicationDto>> getApplicationsByJobForEmployer(
            @PathVariable Long jobId) {
        List<JobApplicationDto> applications = jobService.getApplicationsByJobForEmployer(jobId);
        return ResponseEntity.ok(applications);
    }

    @PatchMapping(path="/applications/employer", version = "1.0")
    public ResponseEntity<String> updatedJobApplication(
            @RequestBody @Valid UpdateJobApplicationDto updateJobApplicationDto) {
        boolean isUpdated = jobService.updateJobApplication(updateJobApplicationDto);
        if (isUpdated)
            return ResponseEntity.ok("Application is updated successfully");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update the application");
    }
}
