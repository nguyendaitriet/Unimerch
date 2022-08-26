package com.unimerch.dto.user;

import com.unimerch.repository.model.Role;
import com.unimerch.util.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserCreateParam {

    @NotBlank(message = "{validation.inputEmpty}")
    @Size(min = 3, max = 80, message = "validation.fullNameLength")
    private String fullName;

    @NotBlank(message = "validation.inputEmpty")
    @Size(min = 3, max = 50, message = "{validation.usernameLength}")
    private String username;

    @NotBlank(message = "{validation.inputEmpty}")
    @Pattern(regexp = ValidationUtils.PASSWORD_REGEX,
            message = "{validation.validPassword}")
    private String password;

}
