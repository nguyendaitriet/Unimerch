package com.unimerch.controller.api;

import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.dto.group.GroupUpdateParam;
import com.unimerch.repository.model.Group;
import com.unimerch.service.GroupService;
import com.unimerch.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupAPI {

    @Autowired
    private GroupService groupService;

    @Autowired
    private AppUtils appUtils;

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping
    public DataTablesOutput<Group> findAllGroupsPageable(@Valid @RequestBody DataTablesInput input) {
        return groupService.findAll(input);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/findAllGroups")
    public ResponseEntity<?> findAllGroups() {
        return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findGroupById(@PathVariable String id) {
        Group group = groupService.findById(id);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@Validated @RequestBody GroupCreateParam groupCreateParam,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        GroupItemResult newGroup = groupService.createGroup(groupCreateParam);
        return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable String id, @RequestBody GroupUpdateParam groupUpdateParam) {
        GroupItemResult group = groupService.updateGroup(id, groupUpdateParam);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        groupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/addAmznAccountToGroup/{id}")
    public ResponseEntity<?> addAmznAccountToGroup(@PathVariable String id,
                                                   @RequestBody Map<String, ArrayList<String>> amznAccIdList) {
        List<AmznAccResult> newAmznAccResultList = groupService
                .addAmznAccToGroup(amznAccIdList.get("amznAccSelected"), id);
        return new ResponseEntity<>(newAmznAccResultList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/showAmznAccInsideGroup/{id}")
    public ResponseEntity<?> showAmznAccInsideGroup(@PathVariable String id) {
        List<AmznAccResult> amznAccResultList = groupService.getAmznAccInsideGroup(id);
        return new ResponseEntity<>(amznAccResultList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
//    @PostMapping("/showAmznAccInsideGroup/{id}")
//    public DataTablesOutput<AmznAccResult> showAmznAccInsideGroup(@PathVariable String id, @Valid @RequestBody DataTablesInput input) {
//        return groupService.getAmznAccInsideGroup(id, input);
//    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/showAmznAccOutsideGroup/{id}")
    public ResponseEntity<?> showAmznAccOutsideGroup(@PathVariable String id) {
        List<AmznAccResult> amznAccResultList = groupService.getAmznAccOutsideGroup(id);
        return new ResponseEntity<>(amznAccResultList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @DeleteMapping("/deleteAmznAccFromGroup/{amznAccId}/{groupId}")
    public ResponseEntity<?> deleteAmznAccFromGroup(@PathVariable int amznAccId, @PathVariable int groupId) {
        groupService.deleteAmznAccFromGroup(amznAccId, groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
