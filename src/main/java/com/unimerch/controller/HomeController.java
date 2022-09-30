package com.unimerch.controller;

import com.unimerch.repository.model.Role;
import com.unimerch.security.RoleConstant;
import com.unimerch.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Autowired
    private PrincipalUtils principalUtils;

    @GetMapping("/")
    public String redirectToDashBoard() {
        try {
            if (principalUtils.getPrincipalRoleCode().equals(RoleConstant.CODE_ADMIN)) {
                return "redirect:/dashboard";
            }
            return "redirect:/users";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        return new ModelAndView("/authentication/login");
    }

}
