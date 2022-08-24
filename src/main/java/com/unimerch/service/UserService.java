package com.unimerch.service;

import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.dto.user.UserCreateParam;
import com.unimerch.dto.user.UserItemResult;
import com.unimerch.repository.model.User;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    DataTablesOutput<UserItemResult> findAllUserDTOExclSelf(DataTablesInput input, String principalUsername);

    User getByUsername(String username);

    User findById(String id);

    UserItemResult findUserListItemById(String id);

    UserItemResult create(UserCreateParam userCreateParam);

    void changePassword(String id, String password);

    void changeMyPassword(String password);

    UserItemResult changeStatus(String id);

    List<GroupItemResult> findAssignedGroups(String userId);

    List<GroupItemResult> findUnassignedGroups(String userId);

    List<GroupItemResult> assignGroupToUser(String userId, List<String> groupId);

    GroupItemResult removeGroupFromUser(String userId, String groupId);
}
