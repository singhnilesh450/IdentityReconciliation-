package com.singhfusion.IdentityReconciliation.controller;

import com.singhfusion.IdentityReconciliation.entity.Contact;
import com.singhfusion.IdentityReconciliation.service.ContactService;
import com.singhfusion.IdentityReconciliation.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;


    @PostMapping("/identify")
    public String PerformIdentification(){
        return  "Welcome!!";
    }
    @GetMapping("/identify")
    public List<Contact> PerformIdentification1(){
        List<Contact> list=contactService.findAll();
        for(Contact c:list)
            System.out.println(c);
        return list;
    }
}
