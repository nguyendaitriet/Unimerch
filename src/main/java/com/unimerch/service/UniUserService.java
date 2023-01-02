package com.unimerch.service;

import com.unimerch.dto.group.GroupResult;
import com.unimerch.dto.user.UserCreateParam;
import com.unimerch.dto.user.UserResult;
import com.unimerch.repository.model.user.User;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UniUserService extends UserDetailsService {
    DataTablesOutput<UserResult> findAllUserDTOExclSelf(DataTablesInput input, String principalUsername);

    User getByUsername(String username);

    User findById(String id);

    UserResult findUserItemResultByUsername(String username);

    UserResult findUserListById(String id);

    UserResult create(UserCreateParam userCreateParam);

    void changePassword(String id, String password);

    void changeMyPassword(String password);

    UserResult changeStatus(String id);

    List<GroupResult> findAssignedGroups(String userId);

    List<GroupResult> findUnassignedGroups(String userId);

    List<GroupResult> assignGroupToUser(String userId, List<String> groupId);

    void removeGroupFromUser(String userId, String groupId);


}
