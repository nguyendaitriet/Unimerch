package com.unimerch.util;

import com.unimerch.repository.model.user.User;
import com.unimerch.service.UniUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class PrincipalUtils {
    @Autowired
    private UniUserService userService;

    public int getPrincipalId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = ((UserDetails) principal).getUsername();
        User user = userService.getByUsername(userName);
        return user.getId();
    }

    public String getPrincipalRoleCode() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = ((UserDetails) principal).getUsername();
        User user = userService.getByUsername(userName);
        return user.getRole().getCode();
    }

    public String getPrincipalUsername() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }


}
