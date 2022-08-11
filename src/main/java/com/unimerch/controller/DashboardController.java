package com.unimerch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/")
    public ModelAndView showDashboardPage() {
        return new ModelAndView("/dashboard/dashboard");
    }

    @GetMapping("/user")
    public ModelAndView showUserManagementPage() {
        return new ModelAndView("/dashboard/user");
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
