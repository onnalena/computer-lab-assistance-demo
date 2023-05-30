package com.tut.idc.library.web.exception;

public class EntityNotFoundException extends RuntimeException {

    public static final String NOT_FOUND_MESSAGE = "%s not found.";
    public static final String AVAILABILITY_MESSAGE = "%s available.";
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
	super(message);
    }

    public EntityNotFoundException(String message, String messageType) {

        super(String.format(messageType, message));
    }
}
