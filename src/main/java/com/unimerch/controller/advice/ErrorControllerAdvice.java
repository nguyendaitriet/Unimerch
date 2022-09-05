package com.unimerch.controller.advice;

import com.unimerch.exception.InvalidIdException;
import com.unimerch.util.PrincipalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ErrorControllerAdvice {
    @Autowired
    private PrincipalUtils principalUtils;

    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String invalidIdException(final InvalidIdException throwable) {
        log.error("Id not found", throwable);
        String role = principalUtils.getPrincipalRoleCode();
        if (role.equals("MANAGER")) {
            return "/error/404";
        }
        return "/error/404-user";
    }
}
