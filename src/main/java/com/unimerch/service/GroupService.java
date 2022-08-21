package com.unimerch.service;

import com.unimerch.dto.amznacc.AmznAccAddedToGroup;
import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupListItem;
import com.unimerch.dto.group.GroupUpdateParam;
import com.unimerch.repository.model.Group;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Group> findAll();

    DataTablesOutput<Group> findAll(DataTablesInput dataTablesInput);

    Optional<Group> findById(String id);

    GroupListItem createGroup(GroupCreateParam groupCreateParam);

    GroupListItem updateGroup(String id, GroupUpdateParam groupUpdateParam);

    void deleteGroup(String id);

    List<AmznAccAddedToGroup> addAmznAccToGroup(ArrayList<String> amznAccIdList, String id);

    List<AmznAccAddedToGroup> getAmznAccInsideGroup(String id);

//    DataTablesOutput<AmznAccAddedToGroup> getAmznAccInsideGroup(String id, DataTablesInput input);

    List<AmznAccAddedToGroup> getAmznAccOutsideGroup(String id);

    void deleteAmznAccFromGroup(int amznAccId, int groupId);

}
