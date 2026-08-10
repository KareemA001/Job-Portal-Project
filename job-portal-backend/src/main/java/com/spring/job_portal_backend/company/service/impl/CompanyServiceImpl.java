package com.spring.job_portal_backend.company.service.impl;

import com.spring.job_portal_backend.constants.ApplicationConstants;
import com.spring.job_portal_backend.dto.CompanyDto;
import com.spring.job_portal_backend.dto.JobDto;
import com.spring.job_portal_backend.entity.Company;
import com.spring.job_portal_backend.entity.Job;
import com.spring.job_portal_backend.repository.CompanyRepository;
import com.spring.job_portal_backend.company.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;


    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company> companyList = companyRepository.findAllCompaniesWithJobsByStatus(ApplicationConstants.ACTIVE_STATUS);
        return companyList.stream().map(this::convertCompanyToDto).collect(Collectors.toList());
    }

    private CompanyDto convertCompanyToDto(Company company) {
        List<JobDto> jobDtos = company.getJobs().stream()
                .map(this::convertJobToDto)
                .collect(Collectors.toList());
        return new CompanyDto(company.getId(), company.getName(), company.getLogo(),
                company.getIndustry(), company.getSize(), company.getRating(),
                company.getLocations(), company.getFounded(), company.getDescription(),
                company.getEmployees(), company.getWebsite(), company.getCreatedAt(), jobDtos);
    }

    private JobDto convertJobToDto(Job job) {
        return new JobDto(
                job.getId(),
                job.getTitle(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getCompany().getLogo(),
                job.getLocation(),
                job.getWorkType(),
                job.getJobType(),
                job.getCategory(),
                job.getExperienceLevel(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getSalaryCurrency(),
                job.getSalaryPeriod(),
                job.getDescription(),
                job.getRequirements(),
                job.getBenefits(),
                job.getPostedDate(),
                job.getApplicationDeadline(),
                job.getApplicationsCount(),
                job.getFeatured(),
                job.getUrgent(),
                job.getRemote(),
                job.getStatus()
        );
    }
}
