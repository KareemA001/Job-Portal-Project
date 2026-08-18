package com.spring.job_portal_backend.job.service;

import com.spring.job_portal_backend.dto.JobDto;
import jakarta.validation.Valid;

import java.util.List;

public interface IJobService {


    List<JobDto> getEmployerJobs(String email);

    JobDto updateJobStatus(Long jobId, String jobStatus, String employerEmail);

    JobDto createJob(@Valid JobDto jobDto, String employerEmail);
}
