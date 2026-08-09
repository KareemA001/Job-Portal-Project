package com.spring.job_portal_backend.repository;

import com.spring.job_portal_backend.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {
}