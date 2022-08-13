package com.unimerch.service;

import com.unimerch.repository.model.Group;
import com.unimerch.repository.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Group> findAll();

    Optional<Group> findById(String id);

    Group createGroup(String groupName);

    Group updateGroup(String id, String groupName);


}
