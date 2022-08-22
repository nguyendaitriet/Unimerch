package com.unimerch.service;

import com.unimerch.repository.model.Role;

public interface RoleService {
    Role findByName(String name);

    boolean isUserAdmin(String id);
}
