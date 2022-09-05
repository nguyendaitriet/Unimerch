package com.unimerch.controller;

import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.service.GroupService;
import com.unimerch.service.UserService;
import com.unimerch.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/users")
public class UserDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PrincipalUtils principalUtils;

    @GetMapping({"", "/"})
    public ModelAndView showDashboardPage() {
        ModelAndView mav = new ModelAndView("/dashboard/dashboard-user");
        int userId = principalUtils.getPrincipalId();
        GroupItemResult group = new GroupItemResult();
        mav.addObject("userId", userId);
        mav.addObject("group", group);

        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView showRevenues(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("/dashboard/dashboard-user");
        int userId = principalUtils.getPrincipalId();
        GroupItemResult group = groupService.findGroupItemResultById(id);
        mav.addObject("group", group);
        mav.addObject("userId", userId);

        return mav;
    }
}
