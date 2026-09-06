package com.spring.job_portal_backend.dto;

public record PostDto(

        Long userId,
        Long id,
        String title,
        String body
) {
}
