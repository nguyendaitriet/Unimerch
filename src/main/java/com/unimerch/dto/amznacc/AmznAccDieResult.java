package com.unimerch.dto.amznacc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class AmznAccDieResult {
    private Integer no;
    private Integer id;
    private String username;
    private String lastUpdate;
    private String accountStatus;
}
