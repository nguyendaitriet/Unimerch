package com.unimerch.security.uni;

import com.unimerch.repository.UserRepository;
import com.unimerch.repository.model.User;
import com.unimerch.security.BeanNameConstant;
import com.unimerch.service.UniUserService;
import com.unimerch.service.impl.UniUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(BeanNameConstant.UNI_USER_SECURITY_SERVICE_NAME)
public class UniUserDetailsService implements UserDetailsService {
    @Autowired
    private UniUserService uniUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UniUserDetailsService.loadUserByUsername");
        User user = uniUserService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No uni found with the given email.");
        }
        return UniUserPrinciple.build(user);
    }

}
