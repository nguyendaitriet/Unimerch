package com.unimerch.security.amznacc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(2)
public class AmznAccSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AmznAccDetailsService amznAccDetailsService() {
		return new AmznAccDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder2() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider2() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(amznAccDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder2());

		return authProvider;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider2());

		http.authorizeRequests()
				.antMatchers("/api/auth/login", "/login").permitAll()
				.antMatchers("/", "/api/users/**", "/api/groups/**").authenticated()
				.antMatchers("/assets/**", "/messages/**").permitAll()
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
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginProcessingUrl("/login/amznAcc")
				.loginPage("/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/")
				.and()
				.logout()
				.logoutUrl("/logout")
				.deleteCookies("JWT")
				.invalidateHttpSession(true)
				.and()
				.csrf().disable();

	}

}
