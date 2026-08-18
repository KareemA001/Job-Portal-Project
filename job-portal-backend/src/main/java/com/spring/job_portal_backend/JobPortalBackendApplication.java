package com.spring.job_portal_backend;

import 	org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auitorAwareImpl")
@EnableCaching
public class JobPortalBackendApplication {

	static void main(String[] args) {
		SpringApplication.run(JobPortalBackendApplication.class, args);
	}
}
