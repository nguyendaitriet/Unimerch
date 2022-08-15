package com.unimerch.service;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.repository.model.Group;
import com.unimerch.repository.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Group> findAll();

    Optional<Group> findById(String id);

    Group createGroup(String groupTitle);

    Group updateGroup(String id, String groupTitle);

    List<AmznAccAddedToGroup> addAmznAccToGroup(ArrayList<String> amznAccIdList, String id);

    List<AmznAccAddedToGroup> getAmznAccInGroup(String id);

}
