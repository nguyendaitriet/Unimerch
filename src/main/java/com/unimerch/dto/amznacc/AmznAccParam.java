package com.unimerch.dto.amznacc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class AmznAccParam {

    @NotBlank(message = "Username must NOT be empty.")
    private String username;

    @NotBlank(message = "Username must NOT be empty.")
    private String password;
}
