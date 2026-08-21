package com.spring.job_portal_backend.user.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.job_portal_backend.constants.ApplicationConstants;
import com.spring.job_portal_backend.dto.*;
import com.spring.job_portal_backend.entity.*;
import com.spring.job_portal_backend.repository.*;
import com.spring.job_portal_backend.user.service.IUserService;
import com.spring.job_portal_backend.utility.ApplicationUtility;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final ProfileRepository profileRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

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

    @Override
    @Transactional
    public ProfileDto createOrUpdateUserProfile(
            String userEmail,
            String profileJson,
            MultipartFile profilePicture,
            MultipartFile resume) throws JsonProcessingException {

        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("This user is not existed"));
        Profile userProfile = user.getProfile();
        if (userProfile == null) {
            userProfile = new Profile();
            userProfile.setUser(user);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        ProfileDto profileDto = objectMapper.readValue(profileJson, ProfileDto.class);
        Profile savedProfile = profileRepository.save(mapToProfile(userProfile, profileDto, profilePicture, resume));
        return mapToProfileDto(savedProfile, false);
    }

    @Override
    public ProfileDto getProfile(String userEmail) {
//        Profile returnedProfile = profileRepository.findProfileByUsername(userEmail)
//                .orElseThrow(() -> new EntityNotFoundException(There is no profile for this user + userEmail));
        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("There is no profile for this user "+ userEmail));
        if (user.getProfile() == null) {
            return null;
        }
        return mapToProfileDto(user.getProfile(), false);
    }

    @Override
    public ProfileDto getProfilePicture(String userEmail) {

        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("There is no profile for this user "+ userEmail));
        if (user.getProfile() == null) {
            return null;
        }
        return mapToProfileDto(user.getProfile(), true);
    }

    @Override
    public ProfileDto getProfileResume(String userEmail) {

        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("There is no profile for this user "+ userEmail));
        if (user.getProfile() == null) {
            return null;
        }
        return mapToProfileDto(user.getProfile(), true);
    }

    @Override
    @Transactional
    public JobDto saveJob(String userEmail, Long id) {
        JobPortalUser user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException());
        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        user.getSavedJobs().add(job);
        return ApplicationUtility.convertJobToDto(job);
    }

    @Override
    @Transactional
    public void unsaveJob(String userEmail, Long id) {
        JobPortalUser user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException());
        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        user.getSavedJobs().remove(job);
    }

    @Override
    public List<JobDto> getAllSavedJobs(String userEmail) {
        JobPortalUser user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException());
        return user.getSavedJobs()
                .stream().map(job -> ApplicationUtility.convertJobToDto(job)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobApplicationDto applayForJob(String userEmail, ApplyJobRequestDto applyJobRequestDto) {
        JobPortalUser user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException());
        Long jobId = applyJobRequestDto.jobId();

        if (jobApplicationRepository.existsByUserIdAndJobId(user.getId(),jobId)) {
            throw new RuntimeException("You have applied for this job before");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("There is no such a job to apply for"));
        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJob(job);
        jobApplication.setAppliedAt(Instant.now());
        jobApplication.setStatus(ApplicationConstants.PENDING_STATUS);
        jobApplication.setCoverLetter(applyJobRequestDto.coverLetter());
        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        job.setApplicationsCount(job.getApplicationsCount() != null ? job.getApplicationsCount() + 1 : 1);
        jobRepository.save(job);

        return mapToJobApplicationDto(savedJobApplication);
    }

    private JobApplicationDto mapToJobApplicationDto(JobApplication savedJobApplication) {
        ProfileDto profileDto = null;
        Profile profile = savedJobApplication.getUser().getProfile();
        if (profile != null) {
            profileDto = new ProfileDto(
                    profile.getId(),
                    profile.getUser().getId(),
                    profile.getJobTitle(),
                    profile.getLocation(),
                    profile.getExperienceLevel(),
                    profile.getProfessionalBio(),
                    profile.getPortfolioWebsite(),
                    profile.getProfilePicture(),
                    profile.getProfilePictureName(),
                    profile.getProfilePictureType(),
                    profile.getResume(),
                    profile.getResumeName(),
                    profile.getResumeType(),
                    profile.getCreatedAt(),
                    profile.getUpdatedAt()
            );
        }
        return new JobApplicationDto(
                savedJobApplication.getId(),
                savedJobApplication.getUser().getId(),
                savedJobApplication.getUser().getName(),
                savedJobApplication.getUser().getEmail(),
                savedJobApplication.getUser().getMobileNumber(),
                profileDto,
                ApplicationUtility.convertJobToDto(savedJobApplication.getJob()),
                savedJobApplication.getAppliedAt(),
                savedJobApplication.getStatus(),
                savedJobApplication.getCoverLetter(),
                savedJobApplication.getNotes()
        );
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

    private Profile mapToProfile(Profile profile, ProfileDto profileDto,
                                 MultipartFile profilePicture, MultipartFile resume) {

        profile.setJobTitle(profileDto.jobTitle());
        profile.setLocation(profileDto.location());
        profile.setExperienceLevel(profileDto.experienceLevel());
        profile.setProfessionalBio(profileDto.professionalBio());
        profile.setPortfolioWebsite(profileDto.portfolioWebsite());

        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                profile.setProfilePicture(profilePicture.getBytes());
                profile.setProfilePictureName(profilePicture.getOriginalFilename());
                profile.setProfilePictureType(profilePicture.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile picture", e);
            }
        }

        if (resume != null && !resume.isEmpty()) {
            try {
                profile.setResume(resume.getBytes());
                profile.setResumeName(resume.getOriginalFilename());
                profile.setResumeType(resume.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload resume", e);
            }
        }
        return profile;
    }

    private ProfileDto mapToProfileDto(Profile profile, boolean includeBinaryData) {
        ProfileDto dto;
        if (includeBinaryData) {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), profile.getProfilePicture(),
                    profile.getProfilePictureName(), profile.getProfilePictureType(), profile.getResume(),
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt()
            );
        } else {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), null,
                    profile.getProfilePictureName(), profile.getProfilePictureType(), null,
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt());
        }
        return dto;
    }
    private UserDto mapToUserDto(JobPortalUser user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        dto.setUserId(user.getId());
        dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
        dto.setCompanyId(user.getCompany() != null ? user.getCompany().getId() : null);
        dto.setCompanyName(user.getCompany() != null ? user.getCompany().getName() : null);
        return dto;
    }
}
