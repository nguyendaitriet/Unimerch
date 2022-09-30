package com.unimerch.controller.api;

import com.unimerch.dto.group.GroupResult;
import com.unimerch.dto.user.UserCreateParam;
import com.unimerch.dto.user.UserResult;
import com.unimerch.security.RoleConstant;
import com.unimerch.service.UniUserService;
import com.unimerch.util.AppUtils;
import com.unimerch.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private UniUserService userService;

    @RoleConstant.ManagerAuthorization
    @PostMapping
    public DataTablesOutput<UserResult> findAllUsersPageableExclSelf(@Valid @RequestBody(required = false) DataTablesInput input) {
        String principalUsername = principalUtils.getPrincipalUsername();
        return userService.findAllUserDTOExclSelf(input, principalUsername);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable String id) {
        UserResult userResult = userService.findUserListById(id);
        return new ResponseEntity<>(userResult, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserCreateParam userCreateParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return AppUtils.mapErrorToResponse(bindingResult);

        UserResult newUser = userService.create(userCreateParam);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changeUserPassword(@PathVariable String id, @RequestBody String newPassword) {
        userService.changePassword(id, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changeMyPassword(@RequestBody String newPassword) {
        userService.changeMyPassword(newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeUserStatus(@PathVariable String id) {
        UserResult userResult = userService.changeStatus(id);
        return new ResponseEntity<>(userResult, HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/asgnGrp/{id}")
    public ResponseEntity<?> findAssignedGroups(@PathVariable String id) {
        List<GroupResult> groupList = userService.findAssignedGroups(id);
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/asgnGrpNot/{id}")
    public ResponseEntity<?> findUnassignedGroups(@PathVariable String id) {
        List<GroupResult> groupList = userService.findUnassignedGroups(id);
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/asgnGrp/{userId}/add")
    public ResponseEntity<?> assignNewGroups(@PathVariable String userId, @RequestBody List<String> listGroupId) {
        List<GroupResult> groupList = userService.assignGroupToUser(userId, listGroupId);
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/asgnGrp/{userId}/remove")
    public ResponseEntity<?> removeOldGroup(@PathVariable String userId, @RequestBody String groupId) {
        userService.removeGroupFromUser(userId, groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
