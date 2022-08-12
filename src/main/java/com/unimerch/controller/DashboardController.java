package com.unimerch.controller;

import com.unimerch.dto.UserListItem;
import com.unimerch.mapper.UserMapper;
import com.unimerch.repository.model.User;
import com.unimerch.service.UserService;
import com.unimerch.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private PrincipalUtils principalUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    public UserListItem getCurrentUser() {
        User currentUser = userService.getByUsername(principalUtils.getPrincipalUsername());
        return userMapper.toUserListItem(currentUser);
    }

    @GetMapping("")
    public ModelAndView showDashboardPage() {
        ModelAndView modelAndView = new ModelAndView("/dashboard/dashboard");
        modelAndView.addObject(getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView showUserManagementPage() {
        ModelAndView modelAndView = new ModelAndView("/dashboard/user");
        modelAndView.addObject(getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/amznAccounts")
    public ModelAndView showAmznAccountManagementPage() {
        ModelAndView modelAndView = new ModelAndView("/dashboard/amzn-account");
        modelAndView.addObject(getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/groups")
    public ModelAndView showGroupManagementPage() {
        ModelAndView modelAndView = new ModelAndView("/dashboard/group");
        modelAndView.addObject(getCurrentUser());
        return modelAndView;
    }

}
