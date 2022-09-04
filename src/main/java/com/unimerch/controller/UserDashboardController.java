package com.unimerch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserDashboardController {
    @GetMapping
    public ModelAndView showUserDashboard() {
        ModelAndView modelAndView = new ModelAndView("/dashboard/dashboard-user");
        return modelAndView;
    }
}
