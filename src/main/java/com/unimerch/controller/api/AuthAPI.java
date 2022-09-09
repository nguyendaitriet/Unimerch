package com.unimerch.controller.api;

import com.unimerch.dto.user.LoginParam;
import com.unimerch.repository.model.JwtResponse;
import com.unimerch.repository.model.User;
import com.unimerch.security.BeanNameConstant;
import com.unimerch.service.UserService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthAPI {

    @Autowired
    @Qualifier(BeanNameConstant.UNI_AUTHENTICATION_MANAGER_NAME)
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginParam user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User currentUser = userService.getByUsername(user.getUsername());
        if (currentUser.isDisabled()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtResponse jwtResponse = new JwtResponse(
                jwt,
                currentUser.getId(),
                userDetails.getUsername(),
                currentUser.getUsername(),
                userDetails.getAuthorities()
        );

        ResponseCookie springCookie = ResponseCookie.from("JWT", jwt)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(60 * 1000)
                .domain("localhost")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(jwtResponse);

    }


}
