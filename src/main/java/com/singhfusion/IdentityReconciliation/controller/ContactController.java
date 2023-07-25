package com.singhfusion.IdentityReconciliation.controller;

import com.singhfusion.IdentityReconciliation.dto.IdentifyRequestDto;
import com.singhfusion.IdentityReconciliation.dto.IdentifyResponseDto;
import com.singhfusion.IdentityReconciliation.entity.Contact;
import com.singhfusion.IdentityReconciliation.service.ContactService;
import com.singhfusion.IdentityReconciliation.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;
@RestController
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }



    @PostMapping(value = "/identify")
    public ResponseEntity<IdentifyResponseDto> identify(@RequestBody @Valid IdentifyRequestDto identifyRequestDto) {
        IdentifyResponseDto identifyResponseDto = contactService.identifyService(identifyRequestDto);
        return new ResponseEntity<>(identifyResponseDto, HttpStatus.CREATED);
    }
//    @GetMapping("/identify")
//    public List<Contact> PerformIdentification1(){
//        List<Contact> list=contactService.findAll();
//        for(Contact c:list)
//            System.out.println(c);
//        return list;
//    }
}
