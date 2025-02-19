package com.code.shopee.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Roles;
import com.code.shopee.model.User;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found!");
        }
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Roles> roles = user.getRoles();
        for(Roles Role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.getRole()));
        }
        return new CustomUserDetails(
                user,
                grantedAuthorities);
    }
}
