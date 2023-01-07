package com.unimerch.controller.api;

import com.unimerch.dto.product.ProductTagTagGroupParam;
import com.unimerch.dto.tag.TagParam;
import com.unimerch.dto.tag.TagResult;
import com.unimerch.repository.model.tag.TagGroup;
import com.unimerch.security.RoleConstant;
import com.unimerch.service.tag.TagService;
import com.unimerch.service.tag.TagGroupService;
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
    private TagGroupService tagGroupService;
    @Autowired
    private TagService tagService;

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/findAllTagGroups")
    public ResponseEntity<?> findAllTagGroups() {
        return new ResponseEntity<>(tagGroupService.findAll(), HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/findAllTagGroupsAndTagsInside")
    public ResponseEntity<?> findAllTagGroupsAndTagsInside() {
        return new ResponseEntity<>(tagGroupService.findAllTagGroupsAndTagsInside(), HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/findAllProductTagsByAsin/{asin}")
    public ResponseEntity<?> findAllProductTagsByAsin(@PathVariable("asin") String asin) {
        return new ResponseEntity<>(tagGroupService.findAllProductTagsByAsin(asin), HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/getAllTags")
    public ResponseEntity<?> getAllTags() {
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/{id}")
    public ResponseEntity<?> findTagGroupById(@PathVariable Integer id) {
        return new ResponseEntity<>(tagGroupService.findById(id), HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/create")
    public ResponseEntity<?> createTagGroup(@RequestBody TagGroup newTagGroup) {
        return new ResponseEntity<>(tagGroupService.createTagGroup(newTagGroup), HttpStatus.CREATED);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/createTag")
    public ResponseEntity<?> createTag(@RequestBody TagParam newTag) {
        return new ResponseEntity<>(tagService.createTag(newTag), HttpStatus.CREATED);
    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/update")
    public ResponseEntity<?> updateTagGroup(@RequestBody TagGroup tagGroup) {
        return new ResponseEntity<>(tagGroupService.updateTagGroup(tagGroup), HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/updateTag")
    public ResponseEntity<?> updateTag(@RequestBody TagParam tag) {
        return new ResponseEntity<>(tagService.updateTag(tag), HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PutMapping("/updateProductTagsByAsin/{asin}")
    public ResponseEntity<?> updateProductTagsByAsin(
            @PathVariable("asin") String asin,
            @RequestBody Map<String, List<ProductTagTagGroupParam>> productTagIdUpdateList
    ) {
        List<ProductTagTagGroupParam> productTagTagGroupParamList = productTagIdUpdateList.get("productTagTagGroupList");
        tagService.updateProductTagsByAsin(asin, productTagTagGroupParamList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTagGroup(@PathVariable Integer id) {
        tagGroupService.deleteTagGroup(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/deleteMultiTag")
    public ResponseEntity<?> deleteMultiTag(@RequestBody Map<String, List<Integer>> tagContentIdList) {
        tagService.deleteMultiTag(tagContentIdList.get("tagContentSelected"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @PostMapping("/addTagToTagGroup/{id}")
    public ResponseEntity<?> addTagToTagGroup(
            @PathVariable Integer id,
            @RequestBody Map<String, ArrayList<Integer>> tagIdList
    ) {
        List<TagResult> newTagResultList = tagGroupService
                .addTagToTagGroup(tagIdList.get("tagContentSelected"), id);
        return new ResponseEntity<>(newTagResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerUserAuthorization
    @GetMapping("/getTagInsideTagGroup/{id}")
    public ResponseEntity<?> getTagInsideTagGroup(@PathVariable Integer id) {
        List<TagResult> tagResultList = tagGroupService.getTagInsideTagGroup(id);
        return new ResponseEntity<>(tagResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @GetMapping("/getTagOutsideTagGroup/{id}")
    public ResponseEntity<?> getTagOutsideTagGroup(@PathVariable Integer id) {
        List<TagResult> tagResultList = tagGroupService.getTagOutsideTagGroup(id);
        return new ResponseEntity<>(tagResultList, HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/deleteTagFromTagGroup/{tagContentId}/{tagId}")
    public ResponseEntity<?> deleteTagFromTagGroup(@PathVariable Integer tagContentId, @PathVariable Integer tagId) {
        tagGroupService.deleteTagFromTagGroup(tagContentId, tagId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleConstant.ManagerAuthorization
    @DeleteMapping("/deleteMultiTagFromTagGroup/{id}")
    public ResponseEntity<?> deleteMultiTagFromTagGroup(
            @PathVariable Integer id,
            @RequestBody Map<String, List<Integer>> tagContentId
    ) {
        tagGroupService.deleteMultiTagFromTagGroup(tagContentId.get("tagContentSelected"), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}