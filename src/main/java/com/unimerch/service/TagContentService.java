package com.unimerch.service;

import com.unimerch.dto.tag_content.TagContentParam;
import com.unimerch.dto.tag_content.TagContentResult;

import java.util.List;

public interface TagContentService {
    List<TagContentResult> findAll();

    TagContentResult findById(int id);

    TagContentResult createTagContent(TagContentParam tagContentParam);

    TagContentResult updateTagContent(TagContentParam tagContentParam);

    void deleteMultiTagContent(List<Integer> tagContentIdList);
}
