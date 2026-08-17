package com.spring.job_portal_backend.company.controller;

import com.spring.job_portal_backend.dto.CompanyDto;
import com.spring.job_portal_backend.company.service.ICompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final ICompanyService companyService;


    @GetMapping(path="/public", version="1.0")
    public ResponseEntity<List<CompanyDto>> getCompaniesVersionOne() {
        List<CompanyDto> companyList = companyService.getAllCompanies();
        return ResponseEntity.ok().body(companyList);
    }


    @PostMapping(path="/admin", version="1.0")
    public ResponseEntity<String> createCompany(@RequestBody @Valid CompanyDto companyDto) {
        boolean isCreated = companyService.creatCompany(companyDto);

        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Creating a new company is completed successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request processing failed");
    }

//    @GetMapping(path="public", version = "2.0")
//    public ResponseEntity<String> getCompaniesVersion2() {
//        return ResponseEntity.ok().body("Companies list from version two");
//    }
}
