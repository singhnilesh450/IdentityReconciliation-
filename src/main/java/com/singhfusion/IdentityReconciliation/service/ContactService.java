package com.singhfusion.IdentityReconciliation.service;

import com.singhfusion.IdentityReconciliation.entity.Contact;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ContactService {
    List<Contact> findAll();
}
