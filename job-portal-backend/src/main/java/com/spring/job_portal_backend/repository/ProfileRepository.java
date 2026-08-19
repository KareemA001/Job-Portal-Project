package com.spring.job_portal_backend.repository;


import com.spring.job_portal_backend.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
