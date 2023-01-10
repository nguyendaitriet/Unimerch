package com.unimerch.service.tag;

import com.unimerch.dto.tag.FullTagGroupTagResult;
import com.unimerch.dto.tag.TagGroupTagIdResult;
import com.unimerch.dto.tag.TagResult;
import com.unimerch.repository.model.tag.TagGroup;

import java.util.List;

public interface TagGroupService {
    List<TagGroup> findAll();

    List<FullTagGroupTagResult> findAllTagGroupsAndTagsInside();

    List<TagGroupTagIdResult> findAllProductTagsByAsin(String asin);

    TagGroup findById(int id);

    TagGroup createTagGroup(TagGroup newTagGroup);

    TagGroup updateTagGroup(TagGroup tagGroup);

    void deleteTagGroup(int id);

    List<TagResult> addTagToTagGroup(List<Integer> tagIdList, int tagGroupId);

    List<TagResult> getTagInsideTagGroup(int tagGroupId);

    List<TagResult> getTagOutsideTagGroup(int tagGroupId);

    void deleteTagFromTagGroup(int tagId, int tagGroupId);

    void deleteMultiTagFromTagGroup(List<Integer> tagIdList, int tagGroupId);
}
