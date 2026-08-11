package com.spring.job_portal_backend.repository;

import com.spring.job_portal_backend.entity.Contact;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {

    List<Contact> findContactByStatus(String status);

    List<Contact> findContactByStatusOrderByCreatedAtAsc(String status);

    List<Contact> findContactByStatus(String status, Sort sortBy);
}
