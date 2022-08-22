package com.unimerch.service;

import com.unimerch.dto.user.UserCreateParam;
import com.unimerch.dto.user.UserListItem;
import com.unimerch.repository.model.Group;
import com.unimerch.repository.model.User;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    DataTablesOutput<UserListItem> findAllUserDTOExclSelf(DataTablesInput input, String principalUsername);

    User getByUsername(String username);

    User findById(String id);

    UserListItem findUserListItemById(String id);

    UserListItem create(UserCreateParam userCreateParam);

    void changePassword(String id, String password);

    void changeMyPassword(String password);

    UserListItem changeStatus(String id);

    List<Group> findAllGrpAssigned(Integer userId);
}
