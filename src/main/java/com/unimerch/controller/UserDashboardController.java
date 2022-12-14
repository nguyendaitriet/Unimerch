package com.unimerch.controller;

import com.unimerch.dto.group.GroupResult;
import com.unimerch.service.group.GroupService;
import com.unimerch.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard-employee")
public class UserDashboardController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private PrincipalUtils principalUtils;

    @GetMapping({"", "/"})
    public ModelAndView showDashboardPage() {
        ModelAndView mav = new ModelAndView("/dashboard/dashboard-user");
        int userId = principalUtils.getPrincipalId();
        GroupResult group = new GroupResult();
        mav.addObject("userId", userId);
        mav.addObject("group", group);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView showRevenues(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("/dashboard/dashboard-user");
        int userId = principalUtils.getPrincipalId();
        GroupResult group = groupService.findGroupItemResultById(id);
        mav.addObject("group", group);
        mav.addObject("userId", userId);
        return mav;
    }

    @GetMapping("/analytics")
    public ModelAndView showUserAnalyticPage() {
        ModelAndView mav = new ModelAndView("/dashboard/analytics-user");
        int userId = principalUtils.getPrincipalId();
        GroupResult group = new GroupResult();
        mav.addObject("userId", userId);
        mav.addObject("group", group);
        return mav;
    }
}
