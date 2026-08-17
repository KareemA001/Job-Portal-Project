package com.spring.job_portal_backend.repository;

import com.spring.job_portal_backend.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {

    List<Contact> findContactByStatus(String status);

    List<Contact> findContactByStatusOrderByCreatedAtAsc(String status);

    List<Contact> findContactByStatus(String status, Sort sortBy);

    Page<Contact> findContactByStatus(String status, Pageable pageable);

    @Modifying
    int updateStatusById(@Param("status") String status, @Param("id") Long id,
                         @Param("updateBy") String updatedBy);
}
