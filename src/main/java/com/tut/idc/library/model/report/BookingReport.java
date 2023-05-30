package com.tut.idc.library.model.report;

import com.tut.idc.library.model.enums.BookingStatus;
import com.tut.idc.library.model.enums.ContactPreference;
import com.tut.idc.library.util.annotation.ColumnName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingReport {
    @ColumnName(name = "Computer Lab")
    private String computerLab;
    @ColumnName(name = "Computer Name")
    private String computerName;
    @ColumnName(name = "ID Number")
    private String IDNumber;
    @ColumnName(name = "Date")
    private String Date;
    @ColumnName(name = "Contact Preference")
    private ContactPreference contactPreference;
    @ColumnName(name = "Status")
    private BookingStatus status;
}
