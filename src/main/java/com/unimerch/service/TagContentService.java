package com.unimerch.service;

import com.unimerch.dto.tag_content.TagContentParam;
import com.unimerch.dto.tag_content.TagContentResult;
import com.unimerch.repository.model.tag.Tag;
import com.unimerch.repository.model.tag.TagContent;

import java.util.List;

public interface TagContentService {
    List<TagContentResult> findAll();

    TagContentResult findById(int id);

    TagContentResult createTagContent(TagContentParam tagContentParam);

    TagContentResult updateTagContent(int id, TagContentParam tagContentParam);

    void deleteMultiTagContent(List<Integer> tagContentIdList);
}
