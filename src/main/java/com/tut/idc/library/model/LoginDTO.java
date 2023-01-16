package com.tut.idc.library.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDTO {

    private String IDNumber;
    private String password;
}
