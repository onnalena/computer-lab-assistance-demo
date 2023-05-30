package com.tut.idc.library.model.report;

import com.tut.idc.library.model.enums.UserStatus;
import com.tut.idc.library.model.enums.UserType;
import com.tut.idc.library.util.annotation.ColumnName;
import lombok.Data;

@Data
public class UserReport {
    @ColumnName(name = "ID number")
    private String IDNumber;

    @ColumnName(name = "First Name")
    private String firstname;

    @ColumnName(name = "Last Name")
    private String lastname;

    @ColumnName(name = "Email")
    private String email;

    @ColumnName(name = "Cellphone No.")
    private String cellPhoneNumber;

    @ColumnName(name = "Status")
    private UserStatus status;

    @ColumnName(name = "User Type")
    private UserType userType;
}
