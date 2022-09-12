package com.unimerch.security.uni;

import com.unimerch.repository.UserRepository;
import com.unimerch.repository.model.User;
import com.unimerch.security.NameConstant;
import com.unimerch.security.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(NameConstant.UNI_USER_SECURITY_SERVICE_NAME)
public class UniUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UniUserDetailsService.loadUserByUsername");
        User user = userRepository.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No uni found with the given email.");
        }
        return UserPrinciple.build(user.getId().toString(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getRole().getCode());
    }

}
