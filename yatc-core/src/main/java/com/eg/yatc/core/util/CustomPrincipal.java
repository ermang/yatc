package com.eg.yatc.core.util;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomPrincipal {

    private final Long id;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomPrincipal(Long id,
                           String username,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}