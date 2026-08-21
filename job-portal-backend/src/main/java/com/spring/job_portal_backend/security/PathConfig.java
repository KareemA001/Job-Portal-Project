package com.spring.job_portal_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathConfig {

    @Bean(name = "publicPaths")
    public List<String> publicPaths() {
        return List.of(

                "/api/contacts/public",
                "/api/auth/login/public",
                "/api/auth/register/public",
                "/api/csrf-token/public",
                "/api/swagger-ui.html",
                "/swagger-ui/**",
                "/api/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }

    @Bean(name = "securedPaths")
    public List<String> securedPaths() {
        return List.of(
                "/api/companies/public",
                "/api/**"
        );
    }

    @Bean(name = "adminPaths")
    public List<String> adminPaths() {
        return List.of(
                "/api/contact/admin",
                "/api/contact/sort/admin",
                "/api/contact/page/admin",
                "/api/contact/${id}/status/admin",
                "/api/companies/admin",
                "/api/companies/{id}/admin",
                "/api/users/search/admin",
                "/api/users/{userId}/role/employer/admin",
                "/api/users/{userId}/company/{companyId}/admin"
        );
    }

    @Bean(name="employerPaths")
    public List<String> employerPaths() {
        return List.of(
                "/api/jobs/employer",
                "/api/{jobId}/status/employer"
        );
    }

    @Bean(name="jobseekerPaths")
    public List<String> jobSeekerPaths() {
        return List.of(
                "/api/users/profile/jobseeker",
                "/api/users/profile/picture/jobseeker",
                "/api/users/profile/resume/jobseeker",
                "/api/users//save-job/{jobId}/jobseeker",
                "/api/users/unsave-job/{jobId}/jobseeker",
                "/api/users/savedJobs/jobseeker"
        );
    }
}
