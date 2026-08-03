package com.spring.job_portal_backend.service.impl;

import com.spring.job_portal_backend.entity.Company;
import com.spring.job_portal_backend.repository.CompanyRepository;
import com.spring.job_portal_backend.service.ICompanyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    @Override
    public List<Company> getAllCompanies() {
        return this.companyRepository.findAll();
    }
}
