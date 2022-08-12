package com.unimerch.service;

import com.unimerch.repository.model.Group;
import org.springframework.stereotype.Service;

public interface GroupService {
    Group createGroup(String groupName);
}
