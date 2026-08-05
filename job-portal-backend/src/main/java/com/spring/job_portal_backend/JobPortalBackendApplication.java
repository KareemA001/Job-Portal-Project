package com.spring.job_portal_backend;

import 	org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auitorAwareImpl")
public class JobPortalBackendApplication {

	static void main(String[] args) {
		SpringApplication.run(JobPortalBackendApplication.class, args);
	}

}
