package com.spring.job_portal_backend.contact.controller;

import com.spring.job_portal_backend.contact.service.impl.ContactServiceImpl;
import com.spring.job_portal_backend.dto.ContactRequestDto;
import com.spring.job_portal_backend.dto.ContactResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path= "/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactServiceImpl contactService;

    @PostMapping(path="public", version = "1.0")
    public ResponseEntity<String> saveContactMessage(@RequestBody @Valid ContactRequestDto contactRequestDto) {
        boolean result = this.contactService.saveContact(contactRequestDto);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("The contact message is saved successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Sorry, try again");
    }

    @GetMapping(path="/admin", version = "1.0")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgs() {
        List<ContactResponseDto> contactResponseDto = contactService.fetchNewContactMsgs();
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDto);
    }

//    @GetMapping(path="public", version = "1.0")
//    public ResponseEntity<String> fetchOpenContacts(@RequestParam
//                                                    @Validated @NotBlank(message = "Status can not be blank")
//                                                    @Size(min = 4,message = "Status length should be of minimum 4 chars")
//                                                        String status) {
//        return ResponseEntity.ok("These are the contacts with the given status: " + status);
//    }
}
