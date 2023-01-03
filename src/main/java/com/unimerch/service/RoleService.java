package com.unimerch.service;

import com.unimerch.repository.model.user.Role;

public interface RoleService {

    Role findByName(String name);

    boolean isUserAdmin(String id);
}
