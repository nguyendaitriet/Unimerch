package com.unimerch.controller;

import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.service.GroupService;
import com.unimerch.service.UniUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private UniUserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping({"", "/"})
    public ModelAndView showDashboardPage() {
        ModelAndView mav = new ModelAndView("/dashboard/dashboard-admin");

        GroupItemResult group = new GroupItemResult();
        group.setTitle(messageSource.getMessage("dashboard.allAccounts", null, Locale.getDefault()));
        mav.addObject("group", group);

        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView showRevenues(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("/dashboard/dashboard-admin");

        GroupItemResult group = groupService.findGroupItemResultById(id);
        mav.addObject("group", group);

        return mav;
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
