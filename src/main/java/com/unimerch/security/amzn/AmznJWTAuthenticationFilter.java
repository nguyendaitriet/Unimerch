package com.unimerch.security.amzn;

import com.unimerch.security.JWTUser;
import com.unimerch.security.NameConstant;
import com.unimerch.service.AmznUserService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(NameConstant.AMZN_JWT_FILTER_NAME)
public class AmznJWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AmznUserService amznUserService;
    @Autowired
    @Qualifier(NameConstant.AMZN_USER_SECURITY_SERVICE_NAME)
    private UserDetailsService userDetailsService;

    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null) {
            if (accessToken.startsWith("Bearer")) {
                accessToken = accessToken.replace(
                      "Bearer ",
                        ""
                );
            }
            return accessToken;
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        setAuthentication(request, accessToken);
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, String accessToken) {
//        if (accessToken.startsWith("Bearer")) {
//            accessToken = accessToken.replace(
//                    "Bearer ",
//                    ""
//            );
//            AmazoneAccounts a = amazoneAccountRepository.findByAccess_token(accessToken).orElseThrow(() -> new UserNotFoundException("Token erorr"));
//            UserDetails userDetails = userDetailsService.loadUserByUsername(a.getEmailAddress());
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities()
//            );
//
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            return;
//        }

        if (jwtService.validateJwtToken(accessToken)) {

            JWTUser jwtUser = jwtService.getPrincipalFromJwtToken(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser.getUsername());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
