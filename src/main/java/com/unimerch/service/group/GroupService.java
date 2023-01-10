package com.unimerch.service.group;

import com.unimerch.dto.amznacc.AmznAccFilterResult;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupResult;
import com.unimerch.dto.group.GroupUpdateParam;
import com.unimerch.repository.model.group.Group;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.ArrayList;
import java.util.List;

public interface GroupService {

    List<GroupResult> findAll();

    DataTablesOutput<GroupResult> findAll(DataTablesInput input);

    Group findById(String id);

    GroupResult findGroupItemResultById(String id);

    GroupResult createGroup(GroupCreateParam groupCreateParam);

    GroupResult updateGroup(String id, GroupUpdateParam groupUpdateParam);

    void deleteGroup(String id);

    List<AmznAccResult> addAmznAccToGroup(ArrayList<String> amznAccIdList, String id);

    List<AmznAccResult> getAmznAccInsideGroup(String id);

    List<AmznAccResult> getAmznAccOutsideGroup(String id);

    void deleteAmznAccFromGroup(int amznAccId, int groupId);

    List<AmznAccFilterResult> findAllAmznAccInGrpFilter(Integer groupId);

    void deleteMultiAmznAccFromGroup(List<Integer> amznAccSelected, Integer id);
}
