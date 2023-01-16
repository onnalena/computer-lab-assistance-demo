package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.ContactPreference;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDTO {

    private String computerLabName;
    private String IDNumber;
    private LocalDateTime dateAndTime;
    private ContactPreference contactPreference;
}
