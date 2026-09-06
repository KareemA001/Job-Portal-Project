package com.spring.job_portal_backend.dto;

public record TodoDto(

         Long userId,
         Long id,
         String title,
         boolean completed
) {
}
