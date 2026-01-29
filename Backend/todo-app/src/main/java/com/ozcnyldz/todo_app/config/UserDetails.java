package com.ozcnyldz.todo_app.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface UserDetails {
    String getUsername();
    String getPassword();
    Collection<? extends GrantedAuthority> getAuthorities();

    boolean isAccountNonExpired();
    boolean isAccountNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();
}