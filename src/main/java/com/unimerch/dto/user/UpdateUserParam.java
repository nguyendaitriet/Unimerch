package com.unimerch.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UpdateUserParam {

    @NotBlank(message = "{validation.inputEmpty}")
    @Size(min = 3, max = 80, message = "{validation.fullNameLength}")
    private String fullName;

    @NotBlank(message = "{validation.inputEmpty}")
    @Size(min = 3, max = 50, message = "{validation.usernameLength}")
    private String username;

    @NotBlank(message = "{validation.inputEmpty}")
    @Size(min = 5, max= 128, message = "{validation.validPassword}")
    private String password;

}
