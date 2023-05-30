package com.tut.idc.library.model.enums;

public enum ReportType {
    USER("User"),
    COMPUTER_LAB("Computer_Lab"),
    COMPUTER("Computer"),
    BOOKING("Booking"),
    FEEDBACK("Feedback");

    private String ofType;

    ReportType(String ofType) {
        this.ofType = ofType;
    }

    public String ofType() {
        return this.ofType;
    }
}
