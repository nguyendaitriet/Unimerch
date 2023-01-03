package com.unimerch.repository.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
    private Integer id;
    private String token;
    private String type = "Bearer";
    private String username;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String accessToken, Integer id, String username, Collection<? extends GrantedAuthority> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
