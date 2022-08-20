package com.unimerch.controller.api;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.repository.model.Group;
import com.unimerch.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupAPI {

    @Autowired
    private GroupService groupService;

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/findAllGroups")
    public ResponseEntity<?> findAllGroups() {
        return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping
    public DataTablesOutput<Group> findAllGroupsPageable(@Valid @RequestBody DataTablesInput input) {
        return groupService.findAll(input);
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
    public ResponseEntity<?> updateGroup(@PathVariable String id, @RequestBody String groupTitle) {
        Group group = groupService.updateGroup(id, groupTitle);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/addAmznAccountToGroup/{id}")
    public ResponseEntity<?> addAmznAccountToGroup(@PathVariable String id,
                                                   @RequestBody Map<String, ArrayList<String>> amznAccIdList) {
        List<AmznAccAddedToGroup> newAmznAccAddedToGroupList = groupService
                .addAmznAccToGroup(amznAccIdList.get("amznAccSelected"), id);
        return new ResponseEntity<>(newAmznAccAddedToGroupList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/showAmznAccInsideGroup/{id}")
    public ResponseEntity<?> showAmznAccInsideGroup(@PathVariable String id) {
        List<AmznAccAddedToGroup> amznAccAddedToGroupList = groupService.getAmznAccInsideGroup(id);
        return new ResponseEntity<>(amznAccAddedToGroupList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
//    @PostMapping("/showAmznAccInsideGroup/{id}")
//    public DataTablesOutput<AmznAccAddedToGroup> showAmznAccInsideGroup(@PathVariable String id, @Valid @RequestBody DataTablesInput input) {
//        return groupService.getAmznAccInsideGroup(id, input);
//    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/showAmznAccOutsideGroup/{id}")
    public ResponseEntity<?> showAmznAccOutsideGroup(@PathVariable String id) {
        List<AmznAccAddedToGroup> amznAccAddedToGroupList = groupService.getAmznAccOutsideGroup(id);
        return new ResponseEntity<>(amznAccAddedToGroupList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @DeleteMapping("/deleteAmznAccFromGroup/{amznAccId}/{groupId}")
    public ResponseEntity<?> deleteAmznAccFromGroup(@PathVariable int amznAccId, @PathVariable int groupId) {
        groupService.deleteAmznAccFromGroup(amznAccId, groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
