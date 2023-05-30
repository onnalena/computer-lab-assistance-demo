package com.tut.idc.library.web.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "%s already exists.";
    public EntityAlreadyExistsException() {
    }

    public EntityAlreadyExistsException(String message) {
        super(String.format(MESSAGE, message));
    }
}
