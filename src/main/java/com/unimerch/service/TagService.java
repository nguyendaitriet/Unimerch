package com.unimerch.service;

import com.unimerch.dto.amznacc.AmznAccFilterResult;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupResult;
import com.unimerch.dto.group.GroupUpdateParam;
import com.unimerch.dto.tag_content.TagContentResult;
import com.unimerch.repository.model.Group;
import com.unimerch.repository.model.Tag;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.ArrayList;
import java.util.List;

public interface TagService {
    List<Tag> findAll();

    Tag findById(int id);

    Tag createTag(Tag newTag);

    Tag updateTag(int id, Tag tag);

    void deleteTag(int id);

    List<TagContentResult> addTagContentToTag(List<Integer> tagContentIdList, int tagId);

    List<TagContentResult> getTagContentInsideTag(int tagId);

    List<TagContentResult> getTagContentOutsideTag(int tagId);

    void deleteTagContentFromTag(int tagContentId, int tagId);

    void deleteMultiTagContentFromTag(List<Integer> tagContentIdList, int tagId);
}
