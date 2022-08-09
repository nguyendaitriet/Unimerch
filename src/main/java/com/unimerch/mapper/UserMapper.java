package com.unimerch.mapper;

import com.unimerch.dto.UserCreateParam;
import com.unimerch.dto.UserCreateResult;
import com.unimerch.repository.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserCreateParam userCreateParam) {
        return new User()
                .setFullName(userCreateParam.getFullName())
                .setUsername(userCreateParam.getUsername());
    }

    public UserCreateResult toUserCreateResult(User user) {
        return new UserCreateResult()
                .setId(user.getId())
                .setFullName(user.getFullName())
                .setUsername(user.getUsername());
    }
}
