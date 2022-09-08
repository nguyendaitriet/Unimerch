package com.unimerch.security.amzn;

import com.unimerch.repository.model.AmznAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AmznUserPrinciple implements UserDetails {

    private AmznAccount amznAccount;

    public AmznUserPrinciple(AmznAccount amznAccount) {
        this.amznAccount = amznAccount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return amznAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return amznAccount.getUsername();
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

