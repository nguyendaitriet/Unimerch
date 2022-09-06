package com.unimerch.service;

import com.unimerch.repository.model.Role;

public interface RoleService {

    String CODE_ADMIN = "MANAGER";
    String CODE_USER = "USER";

    Role findByName(String name);

    boolean isUserAdmin(String id);
}
