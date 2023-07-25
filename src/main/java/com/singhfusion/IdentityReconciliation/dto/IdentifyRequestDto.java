package com.singhfusion.IdentityReconciliation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IdentifyRequestDto {
    String email;
    String phoneNumber;
}