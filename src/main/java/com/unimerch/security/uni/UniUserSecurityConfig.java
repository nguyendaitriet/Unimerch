package com.unimerch.security.uni;

import com.unimerch.security.NameConstant;
import com.unimerch.security.RestAuthenticationEntryPoint;
import com.unimerch.security.handlers.UniAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@Order(1)
public class UniUserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier(NameConstant.UNI_USER_SECURITY_SERVICE_NAME)
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    @Qualifier(NameConstant.UNI_JWT_FILTER_NAME)
    private Filter jwtAuthenticationFilter;

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new UniAuthenticationSuccessHandler();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider1() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean(NameConstant.UNI_AUTHENTICATION_MANAGER_NAME)
    @Primary
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**");
        http.httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authenticationProvider(authenticationProvider1());
        http.authorizeRequests()
                .antMatchers("/api/login", "/login", "/api/amzn/login").permitAll()
//                .antMatchers("/assets/**", "/messages/**").permitAll()
//                .antMatchers("/api/amzn/updateMetadata", "/api/amzn/updateStatus", "/api/orders", "/api/products/update").authenticated()
                .antMatchers("/users/**").hasAnyAuthority("USER")
                .antMatchers("/dashboard/**").hasAnyAuthority("MANAGER")
                .antMatchers("/", "/api/products/**",
                        "/api/users/**",
//                        "/api/orders/**",
//                        "/api/amzn/**",
                        "/api/groups/**")
                .hasAnyAuthority("MANAGER", "USER")
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources/configuration/ui",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/swagger-resources/configuration/security",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**"
                ).permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JWT")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable();
        http.addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
    }
}
