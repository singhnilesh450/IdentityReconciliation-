package com.singhfusion.IdentityReconciliation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContactDTO {
    public Long primaryContactId;
    public List<String> emails;
    public List<String> phoneNumbers;
    public List<Long> secondaryContactIds;
}