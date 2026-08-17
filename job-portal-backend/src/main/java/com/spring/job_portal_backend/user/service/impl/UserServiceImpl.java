package com.spring.job_portal_backend.user.service.impl;

import com.spring.job_portal_backend.dto.UserDto;
import com.spring.job_portal_backend.entity.JobPortalUser;
import com.spring.job_portal_backend.repository.JobPortalUserRepository;
import com.spring.job_portal_backend.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository userRepository;

    @Override
    public Optional<UserDto> searchUserByEmail(String email) {
        return userRepository.findJobPortalUserByEmail(email)
                .map(this::convertToUserDto);
    }

    private UserDto convertToUserDto(JobPortalUser user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        dto.setUserId(user.getId());
        dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
        dto.setCompanyId(user.getCompany() != null ? user.getCompany().getId() : null);
        dto.setCompanyName(user.getCompany() != null ? user.getCompany().getName() : null);
        return dto;
    }
}
