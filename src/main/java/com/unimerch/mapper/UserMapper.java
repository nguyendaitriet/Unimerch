package com.unimerch.mapper;

import com.unimerch.dto.user.UserCreateParam;
import com.unimerch.dto.user.UserItemResult;
import com.unimerch.repository.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserCreateParam userCreateParam) {
        return new User()
                .setFullName(userCreateParam.getFullName())
                .setUsername(userCreateParam.getUsername());
    }

    public UserItemResult toUserItemResult(User user) {
        return new UserItemResult()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setDisabled(user.isDisabled());
    }
}
