package com.tut.idc.library.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDateSelected {
    private String computerLabName;
    private LocalDate selectedDate;
}
