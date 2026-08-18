package com.spring.job_portal_backend.company.service.impl;

import com.spring.job_portal_backend.constants.ApplicationConstants;
import com.spring.job_portal_backend.dto.CompanyDto;
import com.spring.job_portal_backend.dto.JobDto;
import com.spring.job_portal_backend.entity.Company;
import com.spring.job_portal_backend.entity.Job;
import com.spring.job_portal_backend.repository.CompanyRepository;
import com.spring.job_portal_backend.company.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;


    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company> companyList = companyRepository.findAllCompaniesWithJobsByStatus(ApplicationConstants.ACTIVE_STATUS);
        return companyList.stream().map(this::convertCompanyToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean createCompany(CompanyDto companyDto) {
        Company company = convertCompanyDtoToCompany(companyDto);
        company = companyRepository.save(company);
        return company.getId() != null && company.getId() > 0;
    }

    @Override
    @Cacheable(cacheNames = "companies")
    public List<CompanyDto> getAllCompaniesForAdmin() {
        List<Company> companies = this.companyRepository.findAll();
        return companies.stream().map(this::convertCompanyToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = "companies", allEntries = true)
    public boolean updateCompany(Long id, CompanyDto companyDto) {
        int updatedRecords = companyRepository.updateCompanyDetails(
                id,companyDto.name(),companyDto.logo(),
                companyDto.industry(),companyDto.size(),companyDto.rating(),
                companyDto.locations(),companyDto.founded(),companyDto.description(),
                companyDto.employees(),companyDto.website()
        );
        return updatedRecords > 0;
    }

    @Override
    @Transactional
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

    private Company convertCompanyDtoToCompany(CompanyDto companyDto) {
        Company company = new Company();
        BeanUtils.copyProperties(companyDto,company);
        return company;
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
