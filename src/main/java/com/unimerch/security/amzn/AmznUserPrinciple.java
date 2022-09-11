package com.unimerch.security.amzn;

import com.unimerch.repository.model.AmznUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AmznUserPrinciple implements UserDetails {

    private AmznUser amznUser;

    public AmznUserPrinciple(AmznUser amznUser) {
        this.amznUser = amznUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return amznUser.getPassword();
    }

    @Override
    public String getUsername() {
        return amznUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

