package com.unimerch.service.user;

import com.unimerch.dto.group.GroupResult;
import com.unimerch.dto.user.CreateUserParam;
import com.unimerch.dto.user.UserResult;
import com.unimerch.repository.model.user.User;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface UniUserService {
    DataTablesOutput<UserResult> findAllUserDTOExclSelf(DataTablesInput input, String principalUsername);

    User findByUsername(String username);

    User findById(String id);

    UserResult findUserListById(String id);

    UserResult create(CreateUserParam userCreateParam);

    void changePassword(String id, String password);

    void changeMyPassword(String password);

    UserResult changeStatus(String id);

    List<GroupResult> findAssignedGroups(String userId);

    List<GroupResult> findUnassignedGroups(String userId);

    List<GroupResult> assignGroupToUser(String userId, List<String> groupId);

    void removeGroupFromUser(String userId, String groupId);


}
