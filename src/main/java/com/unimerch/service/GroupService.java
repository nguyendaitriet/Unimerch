package com.unimerch.service;

import com.unimerch.dto.amznacc.AmznAccFilterItemResult;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.dto.group.GroupUpdateParam;
import com.unimerch.repository.model.Group;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.ArrayList;
import java.util.List;

public interface GroupService {

    List<GroupItemResult> findAll();

    DataTablesOutput<GroupItemResult> findAll(DataTablesInput input);

    Group findById(String id);

    GroupItemResult findGroupItemResultById(String id);

    GroupItemResult createGroup(GroupCreateParam groupCreateParam);

    GroupItemResult updateGroup(String id, GroupUpdateParam groupUpdateParam);

    void deleteGroup(String id);

    List<AmznAccResult> addAmznAccToGroup(ArrayList<String> amznAccIdList, String id);

    List<AmznAccResult> getAmznAccInsideGroup(String id);

    List<AmznAccResult> getAmznAccOutsideGroup(String id);

    void deleteAmznAccFromGroup(int amznAccId, int groupId);

    List<AmznAccFilterItemResult> findAllAmznAccInGrpFilter(Integer groupId);
}
