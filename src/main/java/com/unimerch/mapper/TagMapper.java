package com.unimerch.mapper;

import com.unimerch.dto.tag.TagParam;
import com.unimerch.dto.tag.TagResult;
import com.unimerch.repository.model.tag.BrgTagGroupTag;
import com.unimerch.repository.model.tag.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagResult toTagResult(BrgTagGroupTag brgTagGroupTag) {
        return new TagResult()
                .setId(brgTagGroupTag.getTag().getId())
                .setName(brgTagGroupTag.getTag().getName());
    }
    public TagResult toTagResult(Tag tag) {
        return new TagResult()
                .setId(tag.getId())
                .setName(tag.getName());
    }
    public Tag toTag(TagParam tagParam) {
        return new Tag()
                .setId(tagParam.getId())
                .setName(tagParam.getName());
    }
}
