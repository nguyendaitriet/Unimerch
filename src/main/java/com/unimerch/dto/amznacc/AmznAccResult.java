package com.unimerch.dto.amznacc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class AmznAccResult {
    private Integer id;
    private String username;
    private String option;

    public AmznAccResult(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}
