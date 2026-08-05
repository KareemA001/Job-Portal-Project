package com.spring.job_portal_backend.contact.service;

import com.spring.job_portal_backend.dto.ContactRequestDto;

public interface IContactService {


    boolean saveContact(ContactRequestDto contactRequestDto);
}
