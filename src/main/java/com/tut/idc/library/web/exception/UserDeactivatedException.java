package com.tut.idc.library.web.exception;

public class UserDeactivatedException extends RuntimeException {
    public static final String DEACTIVATED = "Account was deactivated - please see administrator to reactivate your account.";

    public UserDeactivatedException(String message) {
        super(message);
    }

    public UserDeactivatedException(String message, String messageType) {
        super(String.format(messageType, message));
    }
}
