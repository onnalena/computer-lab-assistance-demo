package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.ContactPreference;
import com.tut.idc.library.persistence.entity.ComputerEntity;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;

@Builder
@Data
public class BookingResponse {
    private String token;
    private ContactPreference contactPreference;
    private String computerLabName;
    private String computerName;
}
