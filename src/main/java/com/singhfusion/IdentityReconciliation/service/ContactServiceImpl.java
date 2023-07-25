package com.singhfusion.IdentityReconciliation.service;

import com.singhfusion.IdentityReconciliation.dao.ContactRepository;
import com.singhfusion.IdentityReconciliation.dto.ContactDTO;
import com.singhfusion.IdentityReconciliation.dto.IdentifyRequestDto;
import com.singhfusion.IdentityReconciliation.dto.IdentifyResponseDto;
import com.singhfusion.IdentityReconciliation.entity.Contact;
import com.singhfusion.IdentityReconciliation.enums.LinkPrecedenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ContactServiceImpl implements  ContactService{
        private final ContactRepository contactRepository;
        private final String dateTimePattern = "dd-MM-yyyy HH:mm:ss.SSS XXX";

        @Autowired
        public ContactServiceImpl(ContactRepository contactRepository) {
            this.contactRepository = contactRepository;
        }

        public IdentifyResponseDto identifyService(IdentifyRequestDto identifyRequestDto) {
            IdentifyResponseDto identifyResponseDto = identifyContactService(identifyRequestDto.getEmail(), identifyRequestDto.getPhoneNumber());
            return  identifyResponseDto;
        }

        public IdentifyResponseDto identifyContactService(String requestedEmail, String requestedPhoneNumber) {
            String sanitizedEmail = sanitizeInput(requestedEmail);
            String sanitizedPhoneNumber = sanitizeInput(requestedPhoneNumber);
            validateInputs(sanitizedEmail, sanitizedPhoneNumber);


            List<Contact> contactList = contactRepository.findByEmailOrPhoneNumber(sanitizedEmail, sanitizedPhoneNumber);

            if (contactList.isEmpty()) {
                // No contact with the requested email or phone number found, create a new primary contact.
                return handleNewContact(sanitizedPhoneNumber, sanitizedEmail);

            } else {
                return handleExistingContacts(sanitizedEmail, sanitizedPhoneNumber, contactList);

            }
        }

        private IdentifyResponseDto handleExistingContacts(String sanitizedEmail, String sanitizedPhoneNumber, List<Contact> contactList) {
            List<Contact> contacts = findRelatedContacts(contactList);

            // Sort contacts based on createdAt timestamp.
            sortByCreatedAt(contacts);

            Contact primaryContact = contacts.get(0);
            if (sanitizedEmail == null || sanitizedPhoneNumber == null) {
                return createContactResponse(primaryContact, contacts);
            }


            // If both email and phone number are present in different contacts, link them.
            linkContactsIfNeeded(sanitizedEmail, sanitizedPhoneNumber, contactList, contacts, primaryContact);
            return createContactResponse(primaryContact, contacts);
        }

        private IdentifyResponseDto handleNewContact(String sanitizedPhoneNumber, String sanitizedEmail) {
                Contact savedContact = saveContactToRepository(sanitizedPhoneNumber, sanitizedEmail, null, LinkPrecedenceType.primary);
                return createContactResponse(savedContact, new ArrayList<>());
            }

        private String sanitizeInput(String input) {
                return (input != null && !input.trim().isEmpty()) ? input.trim() : null;
            }


        private void validateInputs(String email, String phoneNumber) {
            if (email == null && phoneNumber == null) {
                throw new RuntimeException("Invalid Input: Both email and phone number cannot be empty at the same time");
            }
        }
        private List<Contact> findRelatedContacts(List<Contact> contacts) {
            Set<Long> primaryIds = findPrimaryContactIds(contacts);
            List<Contact> relatedContacts = new ArrayList<>();

            for (Long primaryId : primaryIds) {
                relatedContacts.add(contactRepository.findById(primaryId).orElse(null));
                relatedContacts.addAll(contactRepository.findAllByLinkedId(primaryId));
            }

            return relatedContacts;
        }
        private void linkContactsIfNeeded(String email, String phoneNumber, List<Contact> contactList, List<Contact> contacts, Contact primaryContact) {
            boolean phoneIsPresent = false, emailIsPresent = false;

            for (Contact contact : contactList) {
                if (phoneNumber.equals(contact.getPhoneNumber()))
                    phoneIsPresent = true;

                if (email.equals(contact.getEmail()))
                    emailIsPresent = true;

                // If there is already a contact with the same details, no need to go further, just return the response.
                if (email.equals(contact.getEmail()) && phoneNumber.equals(contact.getPhoneNumber())) {
                    return;
                }
            }

            if (phoneIsPresent && emailIsPresent) {
                // Both phone number and email are present in different contacts.
                // Update linkedId and LinkPrecedence if any other primary contact is present.
                for (Contact contact : contacts) {
                    if (!Objects.equals(contact.getId(), primaryContact.getId())
                            && !Objects.equals(contact.getLinkedId(), primaryContact.getId())) {
                        contact.setLinkedId(primaryContact.getId());
                        contact.setLinkPrecedence(LinkPrecedenceType.secondary);
                        contact.setUpdatedAt(getCurrentDateTime());
                        contactRepository.save(contact);
                    }
                }
            } else {
                // If either email or phone number is not present, create a new secondary contact and link it to the primary.
                Contact savedContact = saveContactToRepository(phoneNumber, email, primaryContact.getId(), LinkPrecedenceType.secondary);
                contacts.add(savedContact);
            }
        }

        private IdentifyResponseDto createContactResponse(Contact primaryContact, List<Contact> otherContacts) {
                List<String> emailsList = new ArrayList<>();
                List<String> phoneNumbersList = new ArrayList<>();
                List<Long> secondaryContactIds = new ArrayList<>();

                emailsList.add(primaryContact.getEmail());
                phoneNumbersList.add(primaryContact.getPhoneNumber());

                HashSet<String> uniqueEmails = new HashSet<>();
                HashSet<String> uniquePhones = new HashSet<>();

                uniqueEmails.add(primaryContact.getEmail());
                uniquePhones.add(primaryContact.getPhoneNumber());

                for (Contact contact : otherContacts) {
                    String email = contact.getEmail();
                    String phoneNumber = contact.getPhoneNumber();

                    if (!uniqueEmails.contains(email)) {
                        uniqueEmails.add(email);
                        emailsList.add(email);
                    }

                    if (!uniquePhones.contains(phoneNumber)) {
                        uniquePhones.add(phoneNumber);
                        phoneNumbersList.add(phoneNumber);
                    }

                    if (contact.getLinkPrecedence().equals(LinkPrecedenceType.secondary)) {
                        secondaryContactIds.add(contact.getId());
                    }
                }

                ContactDTO contactDto = ContactDTO.builder()
                        .primaryContactId(primaryContact.getId())
                        .emails(emailsList)
                        .phoneNumbers(phoneNumbersList)
                        .secondaryContactIds(secondaryContactIds)
                        .build();

                return IdentifyResponseDto.builder()
                        .contact(contactDto)
                        .build();
            }

            private Contact saveContactToRepository(String phoneNumber, String email, Long linkedId, LinkPrecedenceType linkPrecedence) {
                String currTime = getCurrentDateTime();
                Contact newContact = Contact.builder()
                        .phoneNumber(phoneNumber)
                        .email(email)
                        .linkPrecedence(linkPrecedence)
                        .linkedId(linkedId)
                        .createdAt(currTime)
                        .updatedAt(currTime)
                        .build();

                return contactRepository.save(newContact);
            }

            private Set<Long> findPrimaryContactIds(List<Contact> contacts) {
                Set<Long> primaryIds = new HashSet<>();
                for (Contact contact : contacts) {
                    Long linkedId = contact.getLinkedId();
                    if (linkedId != null) {
                        primaryIds.add(linkedId);
                    } else {
                        primaryIds.add(contact.getId());
                    }
                }
                return primaryIds;
            }

            private String getCurrentDateTime() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
                return ZonedDateTime.now().format(formatter);
            }

            private void sortByCreatedAt(List<Contact> contacts) {
                contacts.sort(Comparator.comparing(Contact::getCreatedAt));
            }
    }