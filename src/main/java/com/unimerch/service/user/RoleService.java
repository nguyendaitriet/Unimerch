package com.unimerch.service.user;

import com.unimerch.repository.model.user.Role;

public interface RoleService {

    Role findByName(String name);

}
