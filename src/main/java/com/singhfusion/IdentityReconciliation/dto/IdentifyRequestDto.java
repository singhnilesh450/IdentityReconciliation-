package com.singhfusion.IdentityReconciliation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IdentifyRequestDto {
    String email;
    Long phoneNumber;
}