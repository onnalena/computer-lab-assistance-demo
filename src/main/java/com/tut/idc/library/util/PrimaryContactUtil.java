package com.tut.idc.library.util;

import com.tut.idc.library.model.enums.ContactPreference;
import com.tut.idc.library.model.enums.UserContactOption;
import com.tut.idc.library.persistence.entity.UserContactEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
public class PrimaryContactUtil {
    private ContactPreference preferredContact;
    private String contactValue;
    private String subject;
    private String messageContent;

    public PrimaryContactUtil(final ContactPreference preferredContact, final String contactValue) {
        this.preferredContact = preferredContact;
        this.contactValue = contactValue;
    }

    public static PrimaryContactUtil retrievePrimaryContact(final List<UserContactEntity> userContacts) {
        Optional<UserContactEntity> optionalPrimaryContact = userContacts.stream()
                .filter(x -> x.getStatus().equals(UserContactOption.PRIMARY)).findFirst();
        return new PrimaryContactUtil(optionalPrimaryContact.get().getContactPreference(), optionalPrimaryContact.get().getContact());
    }

}
