package com.singhfusion.IdentityReconciliation.service;

import com.singhfusion.IdentityReconciliation.dto.IdentifyRequestDto;
import com.singhfusion.IdentityReconciliation.dto.IdentifyResponseDto;
import com.singhfusion.IdentityReconciliation.entity.Contact;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ContactService {


    IdentifyResponseDto identifyService(IdentifyRequestDto identifyRequestDto);
}
