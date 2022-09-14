package com.unimerch.security.handlers;

import com.unimerch.security.UserPrinciple;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UniLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        String redirectURL = request.getContextPath();

        if (userDetails.hasRole("MANAGER")) {
            redirectURL = "/";
        } else {
            redirectURL = "/users";
        }
        response.sendRedirect(redirectURL);
    }
}
