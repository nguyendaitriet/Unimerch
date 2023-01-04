package com.unimerch.service;

import com.unimerch.dto.tag.TagParam;
import com.unimerch.dto.tag.TagResult;

import java.util.List;

public interface TagService {
    List<TagResult> findAll();

    TagResult findById(int id);

    TagResult createTag(TagParam tagParam);

    TagResult updateTag(TagParam tagParam);

    void deleteMultiTag(List<Integer> tagIdList);
}
