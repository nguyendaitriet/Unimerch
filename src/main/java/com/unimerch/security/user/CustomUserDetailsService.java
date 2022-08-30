package com.unimerch.security.user;

import com.unimerch.repository.UserRepository;
import com.unimerch.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User customer = userRepository.getByUsername(username);
		if (customer == null) {
			throw new UsernameNotFoundException("No customer found with the given email.");
		}
		
		return new UserPrinciple(customer);
	}

}
