package com.unimerch.security.amzn;

import com.unimerch.security.NameConstant;
import com.unimerch.service.AmznUserService;
import com.unimerch.service.UniUserService;
import com.unimerch.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(NameConstant.AMZN_JWT_FILTER_NAME)
public class AmznJWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    @Qualifier(NameConstant.AMZN_USER_SECURITY_SERVICE_NAME)
    private UserDetailsService userDetailsService;

    private String getBearerTokenRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                return authHeader.replace("Bearer ", "");
            }
            return authHeader;
        }

        return null;
    }

    private String getCookieValue(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationType = getAuthorizationType(request);
        if (authorizationType == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (!authorizationType.equals(NameConstant.AMZN_AUTHORIZATION_HEADER_VALUE)) {
                filterChain.doFilter(request, response);
                return;
            }
            String bearerToken = getBearerTokenRequest(request);
            if (bearerToken != null) {
                setAuthentication(request, bearerToken);
            } else {
                String authorizationCookie = getCookieValue(request);
                setAuthentication(request, authorizationCookie);
            }

        } catch (Exception e) {
            logger.error("Can NOT set uni authentication -> Message: {0}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getAuthorizationType(HttpServletRequest request) {
        return request.getHeader("Authorization-Type");
    }

    private void setAuthentication(HttpServletRequest request, String authorizationValue) {
        if (authorizationValue != null && jwtService.validateJwtToken(authorizationValue)) {

            String username = jwtService.getUserNameFromJwtToken(authorizationValue);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

}
