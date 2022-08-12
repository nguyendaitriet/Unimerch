package com.unimerch.controller.api;

import com.unimerch.repository.model.Group;
import com.unimerch.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
public class GroupAPI {

    @Autowired
    private GroupService groupService;

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody String groupName) {
        groupService.createGroup(groupName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
