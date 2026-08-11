package com.spring.job_portal_backend.contact.service.impl;


import com.spring.job_portal_backend.constants.ApplicationConstants;
import com.spring.job_portal_backend.contact.service.IContactService;
import com.spring.job_portal_backend.dto.ContactRequestDto;
import com.spring.job_portal_backend.dto.ContactResponseDto;
import com.spring.job_portal_backend.entity.Contact;
import com.spring.job_portal_backend.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        Contact contact = contactRepository.save(convertToEntity(contactRequestDto));
        if (contact != null && contact.getId() != null)
            return true;
        return false;
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgs() {
        List<Contact> contacts = contactRepository.findContactByStatus(ApplicationConstants.NEW_STATUS);
        List<ContactResponseDto> responseDto = contacts.stream().map(this::transformToDto)
                .collect(Collectors.toList());
        return responseDto;
    }

    private Contact convertToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
//        contact.setCreatedAt(Instant.now());
//        contact.setCreatedBy("System");
        contact.setStatus(ApplicationConstants.NEW_STATUS);
        return contact;
    }

    private ContactResponseDto transformToDto(Contact contact) {
        ContactResponseDto contactResponseDto = new ContactResponseDto(contact.getId(),
                contact.getName(), contact.getEmail(), contact.getUserType(), contact.getSubject(),
                contact.getMessage(), contact.getStatus(), contact.getCreatedAt());
        return contactResponseDto;
    }
}
