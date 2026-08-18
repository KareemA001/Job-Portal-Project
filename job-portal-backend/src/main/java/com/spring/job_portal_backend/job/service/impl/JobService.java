package com.spring.job_portal_backend.job.service.impl;

import com.spring.job_portal_backend.dto.JobDto;
import com.spring.job_portal_backend.entity.Job;
import com.spring.job_portal_backend.entity.JobPortalUser;
import com.spring.job_portal_backend.job.service.IJobService;
import com.spring.job_portal_backend.repository.JobPortalUserRepository;
import com.spring.job_portal_backend.utility.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService implements IJobService {

    private final JobPortalUserRepository jobPortalUserRepository;

    @Override
    public List<JobDto> getEmployerJobs(String email) {
        Optional<JobPortalUser> user = jobPortalUserRepository.findJobPortalUserByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("Employer not found");
        }
        if (user.get().getCompany() == null) {
            throw new RuntimeException("Employer doesn't have a company");
        }

        List<Job> jobs = user.get().getCompany().getJobs();
        return jobs.stream().map(job -> ApplicationUtility.convertJobToDto(job)).collect(Collectors.toList());
    }
}
