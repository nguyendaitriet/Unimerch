package com.unimerch.controller.api;

import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.dto.user.UserCreateParam;
import com.unimerch.dto.user.UserItemResult;
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
    @PostMapping
    public DataTablesOutput<UserItemResult> findAllUsersPageableExclSelf(@Valid @RequestBody(required = false) DataTablesInput input) {
        String principalUsername = principalUtils.getPrincipalUsername();
        return userService.findAllUserDTOExclSelf(input, principalUsername);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable String id) {
        UserItemResult userItemResult = userService.findUserListItemById(id);
        return new ResponseEntity<>(userItemResult, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserCreateParam userCreateParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        UserItemResult newUser = userService.create(userCreateParam);
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
        userService.changeMyPassword(newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeUserStatus(@PathVariable String id) {
       UserItemResult userItemResult = userService.changeStatus(id);
        return new ResponseEntity<>(userItemResult, HttpStatus.OK);
    }

    @GetMapping("/asgnGrp/{id}")
    public ResponseEntity<?> findAssignedGroups(@PathVariable String id) {
        List<GroupItemResult> groupList = userService.findAssignedGroups(id);
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @GetMapping("/asgnGrpNot/{id}")
    public ResponseEntity<?> findUnassignedGroups(@PathVariable String id) {
        List<GroupItemResult> groupList = userService.findUnassignedGroups(id);
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }


    @PostMapping("/asgnGrp/{userId}/add")
    public ResponseEntity<?> assignNewGroups(@PathVariable String userId, @RequestBody List<String> listGroupId) {
        List<GroupItemResult> groupList = userService.assignGroupToUser(userId, listGroupId);
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @DeleteMapping("/asgnGrp/{userId}/remove")
    public ResponseEntity<?> removeOldGroup(@PathVariable String userId, @RequestBody String groupId) {
        userService.removeGroupFromUser(userId, groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
