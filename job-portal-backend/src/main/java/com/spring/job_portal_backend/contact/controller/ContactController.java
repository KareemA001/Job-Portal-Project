package com.spring.job_portal_backend.contact.controller;

import com.spring.job_portal_backend.contact.service.impl.ContactServiceImpl;
import com.spring.job_portal_backend.dto.ContactRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path= "/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactServiceImpl contactService;

    @PostMapping(version = "1.0")
    public ResponseEntity<String> saveContactMessage(@RequestBody @Valid ContactRequestDto contactRequestDto) {
        boolean result = this.contactService.saveContact(contactRequestDto);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("The contact message is saved successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Sorry, try again");
    }

    @GetMapping(version = "1.0")
    public ResponseEntity<String> fetchOpenContacts(@RequestParam
                                                    @Validated @NotBlank(message = "Status can not be blank")
                                                    @Size(min = 4,message = "Status lenght should be of minimum 4 chars")
                                                        String status) {
        return ResponseEntity.ok("These are the contacts with the given status: " + status);
    }
}
