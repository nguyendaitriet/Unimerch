package com.unimerch.controller.api;

import com.unimerch.dto.UserCreateParam;
import com.unimerch.dto.UserListItem;
import com.unimerch.mapper.UserMapper;
import com.unimerch.repository.model.User;
import com.unimerch.service.UserService;
import com.unimerch.util.AppUtils;
import com.unimerch.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageSource messageSource;

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping
    public ResponseEntity<?> findAllUsersExceptCurrent() {
        String principalUsername = principalUtils.getPrincipalUsername();
        List<UserListItem> userListItemList = userService.findAllUsersDTO(principalUsername);
        return new ResponseEntity<>(userListItemList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable String id) {
        User user = userService.findById(id).get();
        UserListItem userListItem = userMapper.toUserListItem(user);
        return new ResponseEntity<>(userListItem, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserCreateParam userCreateParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        userService.create(userCreateParam);
        return new ResponseEntity<>(HttpStatus.CREATED);

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
