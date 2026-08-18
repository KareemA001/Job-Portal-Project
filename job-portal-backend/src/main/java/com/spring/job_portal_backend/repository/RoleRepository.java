package com.spring.job_portal_backend.repository;

import com.spring.job_portal_backend.entity.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Cacheable(cacheNames = "roles")
    Optional<Role> findRoleByName(String name);
}