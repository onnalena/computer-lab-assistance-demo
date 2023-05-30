package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.BookingStatus;
import com.tut.idc.library.model.enums.ContactPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private String computerLabName;
    private String computerName;
    private String IDNumber;
    private String dateTime;
    private ContactPreference contactPreference;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
