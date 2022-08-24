package com.unimerch.controller;

import com.unimerch.repository.model.Group;
import com.unimerch.service.GroupService;
import com.unimerch.service.impl.GroupServiceImpl;
import com.unimerch.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private UserServiceImpl userService;


    @GetMapping("")
    public ModelAndView showDashboardPage() {
        return new ModelAndView("/dashboard/dashboard");
    }

    @GetMapping("/user")
    public ModelAndView showUserManagementPage() {
        return new ModelAndView("/dashboard/user");
    }

    @GetMapping("/user/group/{id}")
    public ModelAndView showAssignGroupToUserPage(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("/dashboard/user-assign-group");

        String username = userService.findUserListItemById(id).getUsername();

        mav.addObject("username", username);
        return mav;
    }

    @GetMapping("/amznAccount")
    public ModelAndView showAmznAccountManagementPage() {
        return new ModelAndView("/dashboard/amzn-account");
    }

    @GetMapping("/group")
    public ModelAndView showGroupManagementPage() {
        return new ModelAndView("/dashboard/group");
    }
}
