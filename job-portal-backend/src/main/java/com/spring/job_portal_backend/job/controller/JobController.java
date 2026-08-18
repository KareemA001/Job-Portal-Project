package com.spring.job_portal_backend.job.controller;

import com.spring.job_portal_backend.dto.JobDto;
import com.spring.job_portal_backend.job.service.IJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/jobs")
@RequiredArgsConstructor
public class JobController {

    private final IJobService jobService;

    @GetMapping(path="/employer", version = "1.0")
    public ResponseEntity<?> getEmployerJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            List<JobDto> jobs = jobService.getEmployerJobs(authentication.getName());
            return ResponseEntity.ok().body(jobs);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please, login first");
    }
}
