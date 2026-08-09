package com.spring.job_portal_backend.repository;

import com.spring.job_portal_backend.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {

    Optional<JobPortalUser> readUserByEmailOrMobileNumber(String email, String mobilNumber);

    Optional<JobPortalUser> findUserByEmail(String email);
}