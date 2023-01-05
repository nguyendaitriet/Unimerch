package com.unimerch.dto.tag;


import com.unimerch.repository.model.tag.Tag;
import com.unimerch.repository.model.tag.TagGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class TagGroupTagResult {
    private Integer tagGroupId;
    private String tagGroupColor;
    private String tagName;
}
