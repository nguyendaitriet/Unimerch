package com.unimerch.security.amznacc;

import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.User;
import com.unimerch.security.user.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AmznAccDetailsService implements UserDetailsService {

	@Autowired private AmznAccountRepository amznAccountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AmznAccount amznAccount = amznAccountRepository.getByUsername(username);
		if (amznAccount == null) {
			throw new UsernameNotFoundException("No amzn acc found with the given username.");
		}
		
		return new AmznAccPrinciple(amznAccount);
	}

}
