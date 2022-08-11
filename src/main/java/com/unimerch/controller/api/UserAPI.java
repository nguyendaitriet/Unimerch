package com.unimerch.controller.api;

import com.unimerch.dto.UserCreateParam;
import com.unimerch.exception.DataInputException;
import com.unimerch.exception.UsernameExistsException;
import com.unimerch.service.UserService;
import com.unimerch.util.AppUtils;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserAPI {

    @Autowired
    AppUtils appUtils;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserCreateParam userCreateParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (userService.existsByUsername(userCreateParam.getUsername())) {
            throw new UsernameExistsException(ValidationUtils.USERNAME_EXISTS);
        }

        try {
            userService.create(userCreateParam);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Account information is not valid, please check the information again");
        }

    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changeUserPassword(@PathVariable String id, @RequestBody String newPassword) {
        userService.changePassword(id, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/disable/{id}")
    public ResponseEntity<?> disableUser(@PathVariable String id) {
        userService.disableUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
