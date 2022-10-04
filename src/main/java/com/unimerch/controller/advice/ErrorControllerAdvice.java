package com.unimerch.controller.advice;

import com.unimerch.exception.InvalidIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class ErrorControllerAdvice {
    @ExceptionHandler(value = {InvalidIdException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView invalidIdException(final InvalidIdException throwable) {
        log.error("Id not found", throwable);
        return new ModelAndView("/error/404");
    }
}
