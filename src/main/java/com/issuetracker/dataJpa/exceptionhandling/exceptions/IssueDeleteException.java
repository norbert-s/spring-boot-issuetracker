package com.issuetracker.dataJpa.exceptionhandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IssueDeleteException extends RuntimeException{
    public IssueDeleteException(String message) {
        super(message);
    }
}
