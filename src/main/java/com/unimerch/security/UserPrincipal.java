package com.unimerch.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails {

    private final String id;

    private final String username;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String id,
                         String username,
                         String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    //Giam Dependency nen ko truyen thang User vao
    public static UserPrincipal build(String id, String username, String passwordHash, String roleCode) {
        List<GrantedAuthority> authorities = null;
        if (roleCode != null) {
            authorities = new ArrayList<>();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleCode);
            authorities.add(authority);
        }
        return new UserPrincipal(
                id,
                username,
                passwordHash,
                authorities
        );
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrincipal user = (UserPrincipal) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
