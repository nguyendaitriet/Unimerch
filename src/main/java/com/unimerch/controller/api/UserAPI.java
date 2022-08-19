package com.unimerch.controller.api;

import com.unimerch.dto.UserCreateParam;
import com.unimerch.dto.UserListItem;
import com.unimerch.service.UserService;
import com.unimerch.util.AppUtils;
import com.unimerch.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAPI {
    @Autowired
    private PrincipalUtils principalUtils;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private UserService userService;

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/findAllNonPageable")
    public ResponseEntity<?> findAllUsersExclSelf() {
        String principalUsername = principalUtils.getPrincipalUsername();
        List<UserListItem> usersList = userService.findAllUsersDTOExclSelf(principalUsername);
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping
    public DataTablesOutput<UserListItem> findAllUsersPageableExclSelf(@Valid @RequestBody(required = false) DataTablesInput input) {
        String principalUsername = principalUtils.getPrincipalUsername();
        return userService.findAllUserDTOExclSelf(input, principalUsername);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable String id) {
        UserListItem userListItem = userService.findUserListItemById(id);
        return new ResponseEntity<>(userListItem, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserCreateParam userCreateParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        UserListItem newUser = userService.create(userCreateParam);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changeUserPassword(@PathVariable String id, @RequestBody String newPassword) {
        userService.changePassword(id, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/changePassword")
    public ResponseEntity<?> changeMyPassword(@RequestBody String newPassword) {
        userService.changePassword(String.valueOf(principalUtils.getPrincipalId()), newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeUserStatus(@PathVariable String id) {
       UserListItem userListItem = userService.changeStatus(id);
        return new ResponseEntity<>(userListItem, HttpStatus.OK);
    }

}
