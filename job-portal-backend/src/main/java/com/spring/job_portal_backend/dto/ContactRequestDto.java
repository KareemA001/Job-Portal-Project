package com.spring.job_portal_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactRequestDto(

        @NotBlank(message = "Email can't be empty")
        @Email(message= "Invalid email address")
        String email,

        @NotBlank(message = "Message can't be empty")
        @Size(max = 500, min = 5, message = "Message must be between 5 and 500 characters")
        String message,

        @NotBlank(message = "Name can't be empty")
        @Size(max = 30, min = 5, message = "Name must be between 5 and 30 characters")
        String name,

        @NotBlank(message = "Subject can't be empty")
        @Size(max = 150, min = 5, message = "Subject must be between 5 and 150 characters")
        String subject,

        @NotBlank(message = "User type can't be empty")
        @Pattern(regexp = "Job Seeker|Employer|Other", message = "User typer must be of type: Job Seeker, Employer or others")
        String userType) {
}
