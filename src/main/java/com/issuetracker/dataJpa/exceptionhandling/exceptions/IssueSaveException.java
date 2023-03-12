package com.issuetracker.dataJpa.exceptionhandling.exceptions;

public class IssueSaveException extends RuntimeException {
    public IssueSaveException(String message) {
        super(message);
    }
}

