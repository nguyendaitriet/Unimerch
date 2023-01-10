package com.unimerch.service.user;

import com.unimerch.repository.user.RoleRepository;
import com.unimerch.repository.model.user.Role;
import com.unimerch.service.user.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
