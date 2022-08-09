package com.unimerch.dto;

import com.unimerch.repository.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserCreateParam {

    @NotBlank(message = "The username is required")
    @Size(min = 5, max = 80, message = "The length of email must be between 5 and 80 characters")
    private String fullName;

    @NotBlank(message = "The username is required")
    @Size(min = 3, max = 50, message = "The length of email must be between 3 and 128 characters")
    private String username;

    @NotBlank(message = "The password is required")
    @Size(max = 128, message = "Maximum password length 128 characters")
    private String password;

//    public UserCreateParam(Long id, String username) {
//        this.id = id;
//        this.username = username;
//    }


}
