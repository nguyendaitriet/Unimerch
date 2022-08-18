package com.unimerch.service;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Group> findAll();

    DataTablesOutput<Group> findAll(DataTablesInput dataTablesInput);

    Optional<Group> findById(String id);

    Group createGroup(String groupTitle);

    Group updateGroup(String id, String groupTitle);

    List<AmznAccAddedToGroup> addAmznAccToGroup(ArrayList<String> amznAccIdList, String id);

//    List<AmznAccAddedToGroup> getAmznAccInsideGroup(String id);
    DataTablesOutput<AmznAccAddedToGroup> getAmznAccInsideGroup(DataTablesInput input);

    List<AmznAccAddedToGroup> getAmznAccOutsideGroup(String id);

    void deleteAmznAccFromGroup(int amznAccId, int groupId);

}
