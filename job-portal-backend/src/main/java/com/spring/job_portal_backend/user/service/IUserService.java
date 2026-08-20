package com.spring.job_portal_backend.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.job_portal_backend.dto.ProfileDto;
import com.spring.job_portal_backend.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface IUserService {

    Optional<UserDto> searchUserByEmail(String email);

    UserDto promoteToEmployer(Long userId);

    UserDto assignCompanyToEmployer(Long userId, Long companyId);

    ProfileDto createOrUpdateUserProfile(String userEmail, String profileJson, MultipartFile profilePhoto, MultipartFile resume)
            throws JsonProcessingException;

    ProfileDto getProfile(String userEmail);

    ProfileDto getProfilePicture(String userEmail);

    ProfileDto getProfileResume(String userEmail);
}
