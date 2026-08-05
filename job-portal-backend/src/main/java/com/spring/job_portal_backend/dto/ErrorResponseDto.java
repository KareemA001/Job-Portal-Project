package com.spring.job_portal_backend.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto(String apiPath, String errorMesssage, HttpStatus errorCode,
                               LocalDateTime errorTime) {
}
