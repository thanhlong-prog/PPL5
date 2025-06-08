package com.code.shopee.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomUserDetails implements UserDetails , OAuth2User {
    private User user;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomUserDetails() {

    }

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super();
        this.user = user;
        this.authorities = authorities;
    }

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.user = user;
        this.authorities = authorities;
        this.attributes = attributes;
    }
    ///////////////////////////////////////////////////
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    public String getAccountName() {
        return user.getName();
    }
    //////////////////////////////////////////////////////
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return authorities;
    }

    @Override
    public String getPassword() {
        
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        
        return user.getUsername();
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
    public boolean isEnabled() {
        return user.getEnable();
    }

    public boolean isVerify() {
        return user.getVerify();
    }

    public String getGmail() {
        return user.getGmail();
    }

}
