package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private LoginType loginType;
    private UserDTO user;
}
