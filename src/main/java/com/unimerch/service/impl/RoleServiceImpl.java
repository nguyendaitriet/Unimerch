package com.unimerch.service.impl;

import com.unimerch.repository.user.RoleRepository;
import com.unimerch.repository.model.Role;
import com.unimerch.repository.model.User;
import com.unimerch.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UniUserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public boolean isUserAdmin(String id) {
        User user = userService.findById(id);
        return user.getRole().getId() == 1;
    }
}
