package com.unimerch.mapper;

import com.unimerch.dto.user.UserCreateParam;
import com.unimerch.dto.user.UserResult;
import com.unimerch.repository.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserCreateParam userCreateParam) {
        return new User()
                .setFullName(userCreateParam.getFullName())
                .setUsername(userCreateParam.getUsername());
    }

    public UserResult toUserResult(User user) {
        return new UserResult()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setDisabled(user.isDisabled());
    }
}
