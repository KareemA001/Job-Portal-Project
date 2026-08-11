package com.spring.job_portal_backend.contact.service;

import com.spring.job_portal_backend.dto.ContactRequestDto;
import com.spring.job_portal_backend.dto.ContactResponseDto;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IContactService {


    boolean saveContact(ContactRequestDto contactRequestDto);

    List<ContactResponseDto> fetchNewContactMsgs();

    List<ContactResponseDto> findNewContactBySortingAndDirection(String attribute, String direction);
}
