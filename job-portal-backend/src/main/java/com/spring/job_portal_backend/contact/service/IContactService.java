package com.spring.job_portal_backend.contact.service;

import com.spring.job_portal_backend.dto.ContactRequestDto;
import com.spring.job_portal_backend.dto.ContactResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IContactService {


    boolean saveContact(ContactRequestDto contactRequestDto);

    List<ContactResponseDto> fetchNewContactMsgs();

    List<ContactResponseDto> findNewContactBySortingAndDirection(String attribute, String direction);

    Page<ContactResponseDto> findNewContactByPagingAndSorting(int index, int size, String attribute, String direction);

    boolean updateMessageToClosed(Long id, String closedStatus);
}
