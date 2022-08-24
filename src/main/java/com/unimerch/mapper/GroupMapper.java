package com.unimerch.mapper;

import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.repository.model.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public Group toGroup(GroupCreateParam groupCreateParam) {
        return new Group().setTitle(groupCreateParam.getTitle());
    }

    public GroupItemResult toGroupItemResult(Group group) {
        return new GroupItemResult()
                .setId(group.getId())
                .setTitle(group.getTitle());
    }


}
