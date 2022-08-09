package com.unimerch.service;

import com.unimerch.repository.model.Role;

public interface RoleService extends GeneralService<Role> {
    Role findByName(String name);
}
