package com.unimerch.mapper;

import com.unimerch.dto.tag_content.TagContentParam;
import com.unimerch.dto.tag_content.TagContentResult;
import com.unimerch.repository.model.tag.BrgTagTagContent;
import com.unimerch.repository.model.tag.TagContent;
import org.springframework.stereotype.Component;

@Component
public class TagContentMapper {

    public TagContentResult toTagContentResult(BrgTagTagContent brgTagTagContent) {
        return new TagContentResult()
                .setId(brgTagTagContent.getTagContent().getId())
                .setName(brgTagTagContent.getTagContent().getName());
    }
    public TagContentResult toTagContentResult(TagContent tagContent) {
        return new TagContentResult()
                .setId(tagContent.getId())
                .setName(tagContent.getName());
    }
    public TagContent toTagContent(TagContentParam tagContentParam) {
        return new TagContent()
                .setId(tagContentParam.getId())
                .setName(tagContentParam.getName());
    }
}
