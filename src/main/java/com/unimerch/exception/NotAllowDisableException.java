package com.unimerch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotAllowDisableException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotAllowDisableException(String message) {
        super(message);
    }
}
