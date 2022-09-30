package com.unimerch.controller.advice;

import com.unimerch.exception.InvalidIdException;
import com.unimerch.exception.ResourceNotFoundException;
import com.unimerch.security.RoleConstant;
import com.unimerch.util.PrincipalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class ErrorControllerAdvice {
    @Autowired
    private PrincipalUtils principalUtils;

    @ExceptionHandler(value = {InvalidIdException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView invalidIdException(final InvalidIdException throwable) {
        log.error("Id not found", throwable);
        String role = principalUtils.getPrincipalRoleCode();
        if (role.equals(RoleConstant.CODE_ADMIN)) {
            return new ModelAndView("/error/404");
        }
        ModelAndView mav = new ModelAndView("/error/404-user");
        mav.addObject("userId",principalUtils.getPrincipalId());
        return mav;
    }

}
