package com.singhfusion.IdentityReconciliation.service;

import com.singhfusion.IdentityReconciliation.dao.ContactRepository;
import com.singhfusion.IdentityReconciliation.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ContactServiceImpl implements  ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }


}
