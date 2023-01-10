package com.unimerch.dto.old_system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Result<T> {
    public T Data;
}
