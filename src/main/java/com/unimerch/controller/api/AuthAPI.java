package com.unimerch.controller.api;

import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.user.LoginParam;
import com.unimerch.dto.user.UserResult;
import com.unimerch.repository.model.user.JwtResponse;
import com.unimerch.security.NameConstant;
import com.unimerch.service.amzn.AmznUserService;
import com.unimerch.service.user.UniUserService;
import com.unimerch.service.user.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthAPI {

    @Autowired
    @Qualifier(NameConstant.UNI_AUTHENTICATION_MANAGER_NAME)
    private AuthenticationManager uniAuthenticationManager;
    @Autowired
    @Qualifier(NameConstant.AMZN_AUTHENTICATION_MANAGER_NAME)
    private AuthenticationManager amznAuthenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UniUserService uniUserService;
    @Autowired
    private AmznUserService amznUserService;

    @PostMapping("/api/login")
    public ResponseEntity<?> uniLogin(@RequestBody LoginParam loginParam) {
        Authentication authentication = uniAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword()));
        UserResult user = uniUserService.findUserItemResultByUsername(loginParam.getUsername());
        return setCookies(authentication, user.getId().toString());
    }

    @PostMapping("/api/amzn/login")
    public ResponseEntity<?> amznLogin(@RequestBody LoginParam loginParam) {
        Authentication authentication = amznAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginParam.getUsername(),
                        loginParam.getPassword()));
        AmznAccResult user = amznUserService.findByUsername(loginParam.getUsername());
        return setCookies(authentication, user.getId().toString());
    }

    private ResponseEntity<?> setCookies(Authentication authentication, String userId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateTokenLogin(userId, userDetails.getUsername());

        JwtResponse jwtResponse = new JwtResponse(accessToken, Integer.parseInt(userId), userDetails.getUsername(), userDetails.getAuthorities());

        ResponseCookie springCookie = ResponseCookie.from("JWT", accessToken)
                .httpOnly(false).secure(false).path("/")
                .maxAge(60 * 60 * 24 * 30)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).body(jwtResponse);
    }
}
