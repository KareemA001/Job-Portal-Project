package com.spring.job_portal_backend.contact.service.impl;


import com.spring.job_portal_backend.constants.ApplicationConstants;
import com.spring.job_portal_backend.contact.service.IContactService;
import com.spring.job_portal_backend.dto.ContactRequestDto;
import com.spring.job_portal_backend.dto.ContactResponseDto;
import com.spring.job_portal_backend.entity.Contact;
import com.spring.job_portal_backend.repository.ContactRepository;
import com.spring.job_portal_backend.utility.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    @Transactional
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        Contact contact = contactRepository.save(convertToEntity(contactRequestDto));
        if (contact != null && contact.getId() != null)
            return true;
        return false;
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgs() {
//        List<Contact> contacts = contactRepository.findContactByStatus(ApplicationConstants.NEW_STATUS);
        List<Contact> contacts = contactRepository.findContactByStatusOrderByCreatedAtAsc(ApplicationConstants.NEW_STATUS);
        List<ContactResponseDto> responseDto = contacts.stream().map(this::transformToDto)
                .collect(Collectors.toList());
        return responseDto;
    }

    @Override
    public List<ContactResponseDto> findNewContactBySortingAndDirection(String attribute, String direction) {
        Sort sort = null;
        if (direction.equalsIgnoreCase("asc"))
            sort = sort.by(attribute).ascending();
        else
            sort = sort.by(attribute).descending();


        List<Contact> contacts = contactRepository.findContactByStatus(ApplicationConstants.NEW_STATUS, sort);
        List<ContactResponseDto> contactResponseDto = contacts.stream().map(this::transformToDto)
                .collect(Collectors.toList());
        return contactResponseDto;
    }

    @Override
    public Page<ContactResponseDto> findNewContactByPagingAndSorting(int index, int size
            , String attribute, String direction) {

        Sort sort = null;
        if (direction.equalsIgnoreCase("asc"))
            sort = sort.by(attribute).ascending();
        else
            sort = sort.by(attribute).descending();
        PageRequest pageRequest = PageRequest.of(index, size, sort);
        Page<Contact> contacts = contactRepository.findContactByStatus(ApplicationConstants.NEW_STATUS, pageRequest);
        Page<ContactResponseDto> contactResponseDto = contacts.map(this::transformToDto);
        return contactResponseDto;
    }

    @Override
    @Transactional
    public boolean updateMessageToClosed(Long id, String closedStatus) {
        int numberOfRows = contactRepository.updateStatusById(closedStatus, id, ApplicationUtility.getLoggedInUser());
        return numberOfRows > 0;
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
