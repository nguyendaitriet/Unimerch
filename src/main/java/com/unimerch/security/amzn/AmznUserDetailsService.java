package com.unimerch.security.amzn;

import com.unimerch.exception.UserNotFoundException;
import com.unimerch.repository.amzn.AmznUserRepository;
import com.unimerch.repository.model.amzn_user.AmznUser;
import com.unimerch.security.NameConstant;
import com.unimerch.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(NameConstant.AMZN_USER_SECURITY_SERVICE_NAME)
public class AmznUserDetailsService implements UserDetailsService {

    @Autowired
    private AmznUserRepository amznUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        AmznUser user = amznUserRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("{exception.userNotFound}"));
        return UserPrincipal.build(
                user.getId().toString(),
                user.getUsername(),
                user.getPassword(),
                null);
    }

}
