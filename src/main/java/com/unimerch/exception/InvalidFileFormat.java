package com.unimerch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class InvalidFileFormat extends RuntimeException{
    public InvalidFileFormat(String message) {
        super(message);
    }

}
