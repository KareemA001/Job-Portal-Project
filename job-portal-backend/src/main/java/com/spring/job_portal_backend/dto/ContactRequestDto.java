package com.spring.job_portal_backend.dto;

public record ContactRequestDto(String email, String message, String name,
                                String subject, String userType) {
}
