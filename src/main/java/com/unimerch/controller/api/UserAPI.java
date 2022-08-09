package com.unimerch.controller.api;

import com.unimerch.dto.UserCreateParam;
import com.unimerch.exception.DataInputException;
import com.unimerch.exception.UsernameExistsException;
import com.unimerch.service.UserService;
import com.unimerch.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserAPI {

    @Autowired
    AppUtils appUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> register(@RequestBody UserCreateParam userCreateParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (userService.existsByUsername(userCreateParam.getUsername())) {
            throw new UsernameExistsException("Username has already existed.");
        }

        try {
            userService.create(userCreateParam);

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Account information is not valid, please check the information again");
        }
    }
}
