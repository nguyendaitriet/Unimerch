package com.unimerch.controller.api;

import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.user.LoginParam;
import com.unimerch.repository.model.JwtResponse;
import com.unimerch.repository.model.User;
import com.unimerch.security.BeanNameConstant;
import com.unimerch.service.AmznUserService;
import com.unimerch.service.UniUserService;
import com.unimerch.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthAPI {

    @Autowired
    @Qualifier(BeanNameConstant.UNI_AUTHENTICATION_MANAGER_NAME)
    private AuthenticationManager uniAuthenticationManager;
    @Autowired
    @Qualifier(BeanNameConstant.AMZN_AUTHENTICATION_MANAGER_NAME)
    private AuthenticationManager amznAuthenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UniUserService uniUserService;
    @Autowired
    private AmznUserService amznUserService;

    @PostMapping("/api/login")
    public ResponseEntity<?> uniLogin(@RequestBody LoginParam loginParam) {
        System.out.println("/api/login");
        Authentication authentication = uniAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword()));
        User user = uniUserService.getByUsername(loginParam.getUsername());
        if (user.isDisabled()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return setCookies(authentication, user.getId().toString());
    }

    @PostMapping("/api/amzn/login")
    public ResponseEntity<?> amznLogin(@RequestBody LoginParam loginParam) {
        System.out.println("/api/amzn/login");
        Authentication authentication = amznAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword()));
        AmznAccResult user = amznUserService.findByUsername(loginParam.getUsername());
        return setCookies(authentication, user.getId().toString());
    }


    private ResponseEntity<?> setCookies(Authentication authentication, String userId) {
        String accessToken = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtResponse jwtResponse = new JwtResponse(accessToken, Integer.parseInt(userId), userDetails.getUsername(), userDetails.getAuthorities());

        ResponseCookie springCookie = ResponseCookie.from("JWT", accessToken).httpOnly(false).secure(false).path("/").maxAge(60 * 1000).domain("localhost").build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).body(jwtResponse);
    }
}
