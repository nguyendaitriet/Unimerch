package com.unimerch.controller.api;

import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.user.LoginParam;
import com.unimerch.security.AccessTokenPrefix;
import com.unimerch.security.NameConstant;
import com.unimerch.security.UserPrincipal;
import com.unimerch.service.user.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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


    @PostMapping(path = "/AccessToken",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> accessToken(HttpServletRequest request) {
        Authentication authentication = amznAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getParameter("UserName"),
                        request.getParameter("Password")));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(principal.getId(), principal.getUsername());
        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        String.format("Bear %s",
                                accessToken
                        )
                ).body(new HashMap<String, String>() {
                    {
                        put("access_token", accessToken);
                    }  });
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> uniLogin(@RequestBody LoginParam loginParam) {
        Authentication authentication = uniAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(principal.getId(), principal.getUsername());
        ResponseCookie cookie = ResponseCookie.from("JWT", accessToken)
                .httpOnly(false).secure(false).path("/")
                .maxAge(60 * 60 * 24 * 30)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/api/amzn/login")
    public ResponseEntity<?> amznLogin(@RequestBody LoginParam loginParam) {
        Authentication authentication = amznAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginParam.getUsername(),
                        loginParam.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(principal.getId(), principal.getUsername());
        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        String.format("%s %s",
                                AccessTokenPrefix.ACCESS_TOKEN_AMZN_PREFIX,
                                accessToken
                        )
                ).build();
    }
}
