package com.spring.job_portal_backend.dto;

public record LoginResponseDto(String message, UserDto user, String jwtToken) {
}
