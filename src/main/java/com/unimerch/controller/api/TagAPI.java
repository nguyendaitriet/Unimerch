package com.unimerch.controller.api;

import com.unimerch.dto.tag_content.TagContentResult;
import com.unimerch.repository.model.tag.Tag;
import com.unimerch.security.RoleConstant;
import com.unimerch.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagAPI {
    @Autowired
    private TagService tagService;

    @RoleConstant.ManagerAuthorization
    @GetMapping("/findAllTags")
    public ResponseEntity<?> findAllGroups() {
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/{id}")
    public ResponseEntity<?> findTagById(@PathVariable Integer id) {
        return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/create")
    public ResponseEntity<?> createTag(@RequestBody Tag newTag) {
        return new ResponseEntity<>(tagService.createTag(newTag), HttpStatus.CREATED);
    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Integer id, @RequestBody Tag tag) {
        return new ResponseEntity<>(tagService.updateTag(id, tag), HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/addTagContentToTag/{id}")
    public ResponseEntity<?> addTagContentToTag(
            @PathVariable Integer id,
            @RequestBody Map<String, ArrayList<Integer>> tagContentIdList
    ) {
        List<TagContentResult> newTagContentResultList = tagService
                .addTagContentToTag(tagContentIdList.get("tagContentSelected"), id);
        return new ResponseEntity<>(newTagContentResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/getTagContentInsideTag/{id}")
    public ResponseEntity<?> getTagContentInsideTag(@PathVariable Integer id) {
        List<TagContentResult> tagContentResultList = tagService.getTagContentInsideTag(id);
        return new ResponseEntity<>(tagContentResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/getTagContentOutsideTag/{id}")
    public ResponseEntity<?> getTagContentOutsideTag(@PathVariable Integer id) {
        List<TagContentResult> tagContentResultList = tagService.getTagContentOutsideTag(id);
        return new ResponseEntity<>(tagContentResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/deleteTagContentFromTag/{tagContentId}/{tagId}")
    public ResponseEntity<?> deleteTagContentFromTag(@PathVariable Integer tagContentId, @PathVariable Integer tagId) {
        tagService.deleteTagContentFromTag(tagContentId, tagId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/deleteMultiTagContentFromTag/{id}")
    public ResponseEntity<?> deleteMultiTagContentFromTag(
            @PathVariable Integer id,
            @RequestBody Map<String, List<Integer>> tagContentIdList
    ) {
        tagService.deleteMultiTagContentFromTag(tagContentIdList.get("tagContentSelected"), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}