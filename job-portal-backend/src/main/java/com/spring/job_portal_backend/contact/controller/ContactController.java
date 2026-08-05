package com.spring.job_portal_backend.contact.controller;

import com.spring.job_portal_backend.contact.service.impl.ContactServiceImpl;
import com.spring.job_portal_backend.dto.ContactRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path= "/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactServiceImpl contactService;

    @PostMapping(version = "1.0")
    public ResponseEntity<String> saveContactMessage(@RequestBody ContactRequestDto contactRequestDto) {
        boolean result = this.contactService.saveContact(contactRequestDto);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("The contact message is saved successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Sorry, try again");
    }
}
