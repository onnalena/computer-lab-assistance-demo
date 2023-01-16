package com.tut.idc.library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tut.idc.library.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private String IDNumber;
    private String firstname;
    private String lastname;
    private List<UserContactDTO> userContacts;

    private UserStatus status;
    private String password;
}
