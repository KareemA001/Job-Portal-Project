package com.spring.job_portal_backend.company.controller;

import com.spring.job_portal_backend.dto.CompanyDto;
import com.spring.job_portal_backend.company.service.ICompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        boolean isCreated = companyService.createCompany(companyDto);

        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Creating a new company is completed successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request processing failed");
    }

    @GetMapping(path="/admin", version="1.0")
    public ResponseEntity<List<CompanyDto>> getAllCompaniesForAdmin() {
        List<CompanyDto> companyDtoList = companyService.getAllCompaniesForAdmin();
        if (companyDtoList != null)
            return ResponseEntity.ok(companyDtoList);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping(path="/{id}/admin", version="1.0")
    public ResponseEntity<String> updateCompany(@PathVariable(name = "id") Long id,
                                                @RequestBody @Valid CompanyDto companyDto ) {

        boolean isUpdated = companyService.updateCompany(id, companyDto);
        if (isUpdated) {
            return ResponseEntity.ok().body("Request processing completed successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request processing failed");
    }

    @DeleteMapping(path = "/{id}/admin", version = "1.0")
    public ResponseEntity<String> deleteCompanyById(@PathVariable @NotBlank String id) {
        companyService.deleteCompanyById(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Company record deleted successfully.");
    }

//    @GetMapping(path="public", version = "2.0")
//    public ResponseEntity<String> getCompaniesVersion2() {
//        return ResponseEntity.ok().body("Companies list from version two");
//    }
}
