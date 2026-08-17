package com.spring.job_portal_backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CompanyDto(

        Long id,

        @NotBlank(message = "Company name can't be empty")
        String name,

        @NotBlank(message = "Company logo can't be empty")
        String logo,

        @NotBlank(message = "Company industry can't be empty")
        String industry,

        @NotBlank(message = "Company size can't be empty")
        String size,

        @DecimalMax(value = "5.0", message = "Rating must be at max 5.0")
        @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
        BigDecimal rating,

        @NotBlank(message = "Company location can't be empty")
        String locations,

        @Min(value = 1900, message = "Company foundation history can't be before 1900")
        Integer founded,

        @NotBlank(message = "Company description can't be empty")
        String description,

        @Min(value = 1, message = "Company employers number can't be less than 1")
        Integer employees,

        @NotBlank(message = "Company website can't be empty")
        String website,

        Instant createdAt,

        List<JobDto> jobs) {

}
