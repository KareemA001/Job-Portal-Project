package com.spring.job_portal_backend.company.controller;

import com.spring.job_portal_backend.entity.Company;
import com.spring.job_portal_backend.service.ICompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/companies")
public class CompanyController {

    private final ICompanyService companyService;

    public CompanyController(ICompanyService companyService) {
        this.companyService = companyService;
    }
    @GetMapping(version="1.0")
    public ResponseEntity<List<Company>> getCompaniesVersionOne() {
        List<Company> companyList = companyService.getAllCompanies();
        return ResponseEntity.ok().body(companyList);
    }

    @GetMapping(version = "2.0")
    public ResponseEntity<String> getCompaniesVersion2() {
        return ResponseEntity.ok().body("Companies list from version two");
    }
}
