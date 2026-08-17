package com.spring.job_portal_backend.user.service;

import com.spring.job_portal_backend.dto.UserDto;

import java.util.Optional;

public interface IUserService {

    Optional<UserDto> searchUserByEmail(String email);

    UserDto promoteToEmployer(Long userId);

    UserDto assignCompanyToEmployer(Long userId, Long companyId);
}
