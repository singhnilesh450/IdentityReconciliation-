package com.singhfusion.IdentityReconciliation.dao;

import com.singhfusion.IdentityReconciliation.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    public List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);
    public List<Contact> findAllByLinkedId(Long linkedId);
}
