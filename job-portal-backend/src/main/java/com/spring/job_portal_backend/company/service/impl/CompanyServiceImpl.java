package com.spring.job_portal_backend.company.service.impl;

import com.spring.job_portal_backend.dto.CompanyDto;
import com.spring.job_portal_backend.entity.Company;
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
        List<Company> companyList = companyRepository.findAll();
        return companyList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private CompanyDto convertToDto(Company company) {
        return new CompanyDto(company.getId(), company.getName(), company.getLogo(),
                company.getIndustry(), company.getSize(), company.getRating(),
                company.getLocations(), company.getFounded(), company.getDescription(),
                company.getEmployees(), company.getWebsite(), company.getCreatedAt());
    }
}
