package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.ContactPreference;
import com.tut.idc.library.model.enums.UserContactOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContactDTO {
    private String IDNumber;
    private String contact;
    private ContactPreference contactPreference;
    private UserContactOption status;
}
