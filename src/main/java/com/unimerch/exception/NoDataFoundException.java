package com.unimerch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NoDataFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NoDataFoundException(String message) {
        super(message);
    }
}
