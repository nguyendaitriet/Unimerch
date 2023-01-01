package com.unimerch.mapper;

import com.unimerch.dto.user.CreateUserParam;
import com.unimerch.dto.user.UpdateUserParam;
import com.unimerch.dto.user.UserResult;
import com.unimerch.repository.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toModel(CreateUserParam userCreateParam) {
        return new User()
                .setFullName(userCreateParam.getFullName())
                .setUsername(userCreateParam.getUsername());
    }
    public User toModel(UpdateUserParam updateUserParam) {
        return new User()
                .setFullName(updateUserParam.getFullName())
                .setUsername(updateUserParam.getUsername());
    }

    public UserResult toDTO(User user) {
        return new UserResult()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setDisabled(user.isDisabled());
    }
}
