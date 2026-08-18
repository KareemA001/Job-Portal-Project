package com.spring.job_portal_backend.job.service.impl;

import com.spring.job_portal_backend.dto.JobDto;
import com.spring.job_portal_backend.entity.Job;
import com.spring.job_portal_backend.entity.JobPortalUser;
import com.spring.job_portal_backend.job.service.IJobService;
import com.spring.job_portal_backend.repository.JobPortalUserRepository;
import com.spring.job_portal_backend.repository.JobRepository;
import com.spring.job_portal_backend.utility.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService implements IJobService {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final JobRepository jobRepository;

    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {

        JobPortalUser employer = jobPortalUserRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }

        List<Job> jobs = employer.getCompany().getJobs();
        return jobs.stream()
                .map(job -> ApplicationUtility.convertJobToDto(job))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobDto updateJobStatus(Long jobId, String jobStatus, String employerEmail) {

        if (!jobStatus.equals("ACTIVE") && !jobStatus.equals("CLOSED") && !jobStatus.equals("DRAFT")) {
            throw new RuntimeException("Invalid status. Must be ACTIVE, CLOSED, or DRAFT");
        }
        JobPortalUser employer = jobPortalUserRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }
        Job job = employer.getCompany().getJobs().stream().filter(j -> j.getId().equals(jobId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(jobStatus);
        return ApplicationUtility.convertJobToDto(job);
    }

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {

        JobPortalUser employer = jobPortalUserRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned. Please contact admin.");
        }

        Job job = convertDtoToEntity(jobDto);
        job.setPostedDate(Instant.now());
        job.setApplicationsCount(0);
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedJob = jobRepository.save(job);
        return ApplicationUtility.convertJobToDto(savedJob);
    }

    private Job convertDtoToEntity(JobDto jobDto) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);
        return job;
    }

}
