package com.spring.job_portal_backend.user.service.impl;

import com.spring.job_portal_backend.constants.ApplicationConstants;
import com.spring.job_portal_backend.dto.UserDto;
import com.spring.job_portal_backend.entity.Company;
import com.spring.job_portal_backend.entity.JobPortalUser;
import com.spring.job_portal_backend.entity.Role;
import com.spring.job_portal_backend.repository.CompanyRepository;
import com.spring.job_portal_backend.repository.JobPortalUserRepository;
import com.spring.job_portal_backend.repository.RoleRepository;
import com.spring.job_portal_backend.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Optional<UserDto> searchUserByEmail(String email) {
        return userRepository.findJobPortalUserByEmail(email)
                .map(this::convertToUserDto);
    }

    @Override
    @Transactional
    public UserDto promoteToEmployer(Long userId) {
        JobPortalUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (ApplicationConstants.ROLE_EMPLOYER.equals(user.getRole().getName())) {
            return convertToUserDto(user);
        }
        if (ApplicationConstants.ROLE_ADMIN.equals(user.getRole().getName())) {
            throw new RuntimeException("Cannot elevate admin user to employer role");
        }

        Role employerRole = roleRepository.findRoleByName(ApplicationConstants.ROLE_EMPLOYER)
                .orElseThrow(() -> new RuntimeException("ROLE_EMPLOYER not found"));
        user.setRole(employerRole);

        /*
         * findById() returns a managed entity
         * You modify it inside a transaction
         * Dirty checking automatically updates it
         */

        // JobPortalUser updatedUser = userRepository.save(user);

        return convertToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto assignCompanyToEmployer(Long userId, Long companyId) {
        JobPortalUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        if (!ApplicationConstants.ROLE_EMPLOYER.equals(user.getRole().getName())) {
            throw new RuntimeException("User must be an employer to be assigned to a company");
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        user.setCompany(company);
        // JobPortalUser updatedUser = userRepository.save(user);
        return convertToUserDto(user);
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
