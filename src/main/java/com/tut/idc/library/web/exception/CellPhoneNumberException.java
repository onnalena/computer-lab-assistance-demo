package com.tut.idc.library.web.exception;

public class CellPhoneNumberException extends RuntimeException {
    public static final String NOT_VALID = "Invalid cellphone number.";

    public CellPhoneNumberException(String message) {
        super(message);
    }

    public CellPhoneNumberException(String message, String messageType) {
        super(String.format(messageType, message));
    }
}
