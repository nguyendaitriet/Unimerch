package com.unimerch.security.amzn;

import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.security.BeanNameConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(BeanNameConstant.AMZN_USER_SECURITY_SERVICE_NAME)
public class AmznUserDetailsService implements UserDetailsService {

	@Autowired private AmznAccountRepository amznAccountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AmznAccount amznAccount = amznAccountRepository.getByUsername(username);
		if (amznAccount == null) {
			throw new UsernameNotFoundException("No amzn acc found with the given username.");
		}
		
		return new AmznUserPrinciple(amznAccount);
	}

}
