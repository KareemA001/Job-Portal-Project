package com.spring.job_portal_backend.contact.service.impl;


import com.spring.job_portal_backend.contact.service.IContactService;
import com.spring.job_portal_backend.dto.ContactRequestDto;
import com.spring.job_portal_backend.entity.Contact;
import com.spring.job_portal_backend.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

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

    private Contact convertToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
        contact.setCreatedAt(Instant.now());
        contact.setCreatedBy("System");
        contact.setStatus("NEW");
        return contact;
    }
}
