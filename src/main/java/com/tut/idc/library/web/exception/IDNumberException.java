package com.tut.idc.library.web.exception;

public class IDNumberException extends RuntimeException {
    public static final String NOT_VALID = "Invalid ID Number - %s not valid.";
    public static final String NOT_LEAP_YEAR = "Invalid ID Number - Year is not a leap year - {%s} is not valid.";

    public IDNumberException(String message) {
        super(message);
    }

    public IDNumberException(String message, String messageType) {
        super(String.format(messageType, message));
    }
}
