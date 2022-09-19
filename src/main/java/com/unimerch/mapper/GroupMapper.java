package com.unimerch.mapper;

import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupResult;
import com.unimerch.repository.model.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public Group toGroup(GroupCreateParam groupCreateParam) {
        return new Group().setTitle(groupCreateParam.getTitle());
    }

    public GroupResult toGroupResult(Group group) {
        return new GroupResult()
                .setId(group.getId())
                .setTitle(group.getTitle());
    }


}
