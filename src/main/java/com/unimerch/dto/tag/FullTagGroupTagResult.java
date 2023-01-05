package com.unimerch.dto.tag;


import com.unimerch.repository.model.tag.TagGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class FullTagGroupTagResult {
    private TagGroup tagGroup;
    private List<TagResult> tagResultList;
}
