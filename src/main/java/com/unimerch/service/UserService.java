package com.unimerch.service;

import com.unimerch.dto.UserCreateParam;
import com.unimerch.dto.UserCreateResult;
import com.unimerch.dto.UserListItem;
import com.unimerch.repository.model.User;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<UserListItem> findAllUsersDTOExclSelf(String principalUsername);

    DataTablesOutput<UserListItem> findAllUserDTOExclSelf(DataTablesInput input, String principalUsername);

    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findById(String id);

    UserListItem findUserListItemById(String id);

    UserCreateResult create(UserCreateParam userCreateParam);

    void changePassword(String id, String password);

    UserListItem changeStatus(String id);
}
