package com.unimerch.service;

import com.unimerch.dto.UserCreateParam;
import com.unimerch.dto.UserCreateResult;
import com.unimerch.dto.UserDTO;
import com.unimerch.repository.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    UserCreateResult create(UserCreateParam userCreateParam);

    boolean existsByUsername(String username);

    void changePassword(String id, String password);

    void disableUser(String id);

    List<UserDTO> findAllUsersDTO(String principalUsername);

}
