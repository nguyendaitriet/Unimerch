package com.unimerch.controller.api;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.repository.model.Group;
import com.unimerch.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class GroupAPI {

    @Autowired
    private GroupService groupService;

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping
    public ResponseEntity<?> findAllGroups() {
        return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findGroupById(@PathVariable String id) {
        Optional<Group> group = groupService.findById(id);
        return new ResponseEntity<>(group.get(), HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody String groupTitle) {
        Group newGroup = groupService.createGroup(groupTitle);
        return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable String id ,@RequestBody String groupTitle) {
        Group group = groupService.updateGroup(id, groupTitle);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")

    @PostMapping("/addAmznAccount/{id}")
    public ResponseEntity<?> addAmznAccount(@PathVariable String id, @RequestBody ArrayList<String> amznAccIdList) {
        List<AmznAccAddedToGroup> newAmznAccAddedToGroupList = groupService.addAmznAccToGroup(amznAccIdList, id);
        return new ResponseEntity<>(newAmznAccAddedToGroupList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/showAmznAccInGroup/{id}")
    public ResponseEntity<?> showAmznAccInGroup(@PathVariable String id) {
        List<AmznAccAddedToGroup> amznAccAddedToGroupList = groupService.getAmznAccInGroup(id);
        return new ResponseEntity<>(amznAccAddedToGroupList, HttpStatus.OK);
    }

//    @GetMapping("/show")

}
