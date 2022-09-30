package com.unimerch.controller.api;

import com.unimerch.dto.amznacc.AmznAccFilterResult;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupResult;
import com.unimerch.dto.group.GroupUpdateParam;
import com.unimerch.repository.model.Group;
import com.unimerch.security.RoleConstant;
import com.unimerch.service.GroupService;
import com.unimerch.util.AppUtils;
import lombok.RequiredArgsConstructor;
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
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupAPI {

    @Autowired
    private GroupService groupService;

    @RoleConstant.ManagerAuthorization
    @PostMapping
    public DataTablesOutput<GroupResult> findAllGroupsPageable(@Valid @RequestBody DataTablesInput input) {
        return groupService.findAll(input);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/findAllGroups")
    public ResponseEntity<?> findAllGroups() {
        return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/{id}")
    public ResponseEntity<?> findGroupById(@PathVariable String id) {
        Group group = groupService.findById(id);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@Validated @RequestBody GroupCreateParam groupCreateParam,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return AppUtils.mapErrorToResponse(bindingResult);
        }
        GroupResult newGroup = groupService.createGroup(groupCreateParam);
        return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable String id, @RequestBody GroupUpdateParam groupUpdateParam) {
        GroupResult group = groupService.updateGroup(id, groupUpdateParam);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        groupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/addAmznAccountToGroup/{id}")
    public ResponseEntity<?> addAmznAccountToGroup(@PathVariable String id,
                                                   @RequestBody Map<String, ArrayList<String>> amznAccIdList) {
        List<AmznAccResult> newAmznAccResultList = groupService
                .addAmznAccToGroup(amznAccIdList.get("amznAccSelected"), id);
        return new ResponseEntity<>(newAmznAccResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/showAmznAccInsideGroup/{id}")
    public ResponseEntity<?> showAmznAccInsideGroup(@PathVariable String id) {
        List<AmznAccResult> amznAccResultList = groupService.getAmznAccInsideGroup(id);
        return new ResponseEntity<>(amznAccResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/showAmznAccOutsideGroup/{id}")
    public ResponseEntity<?> showAmznAccOutsideGroup(@PathVariable String id) {
        List<AmznAccResult> amznAccResultList = groupService.getAmznAccOutsideGroup(id);
        return new ResponseEntity<>(amznAccResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/deleteAmznAccFromGroup/{amznAccId}/{groupId}")
    public ResponseEntity<?> deleteAmznAccFromGroup(@PathVariable int amznAccId, @PathVariable int groupId) {
        groupService.deleteAmznAccFromGroup(amznAccId, groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/deleteAmznAccFromGroup/{id}")
    public ResponseEntity<?> deleteMultiAmznAccFromGroup(@RequestBody Map<String, List<Integer>> amznAccIdList, @PathVariable Integer id) {
        groupService.deleteMultiAmznAccFromGroup(amznAccIdList.get("amznAccSelected"), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/findInGrpFilter/{id}")
    public ResponseEntity<?> findAllAmznAccountsInGroup(@PathVariable Integer id) {
        List<AmznAccFilterResult> amznAccResultList = groupService.findAllAmznAccInGrpFilter(id);
        return new ResponseEntity<>(amznAccResultList, HttpStatus.OK);
    }

}
