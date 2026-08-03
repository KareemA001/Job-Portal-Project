package com.spring.job_portal_backend.company.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/companies")
public class CompanyController {

    @GetMapping(version="1.0")
    public ResponseEntity<String> getCompaniesVersionOne() {
        return ResponseEntity.ok().body("Companies list from version one");
    }

    @GetMapping(version = "2.0")
    public ResponseEntity<String> getCompaniesVersion2() {
        return ResponseEntity.ok().body("Companies list from version two");
    }
}
