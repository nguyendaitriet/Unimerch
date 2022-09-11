package com.unimerch.security.amzn;

import com.unimerch.repository.AmznUserRepository;
import com.unimerch.repository.model.AmznUser;
import com.unimerch.security.BeanNameConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(BeanNameConstant.AMZN_USER_SECURITY_SERVICE_NAME)
public class AmznUserDetailsService implements UserDetailsService {

    @Autowired
    private AmznUserRepository amznUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("AmznUserDetailsService.loadUserByUsername");
        AmznUser user = amznUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No amzn acc found with the given username.");
        }

        return new AmznUserPrinciple(user);
    }

}
