package com.unimerch.service.impl;

import com.unimerch.exception.ServerErrorException;
import com.unimerch.repository.GroupRepository;
import com.unimerch.repository.model.Group;
import com.unimerch.service.GroupService;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Group createGroup(String groupName) {


        try {
            Group newGroup = new Group(0, groupName.trim());
            return groupRepository.save(newGroup);
        } catch (Exception e) {
            throw new ServerErrorException(ValidationUtils.SERVER_ERROR);
        }
    }
}
