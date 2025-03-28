package com.code.shopee.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.code.shopee.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //cho user
    @Order(1)
    @Bean
    SecurityFilterChain UserSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .securityMatcher("/buyer/**", "/login", "/register")
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/register").permitAll()
            .requestMatchers("/buyer/**").hasAuthority("buyer")
            .anyRequest().authenticated()
        ).formLogin(login->login 
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/buyer/home",true)
        )
        .userDetailsService(customUserDetailService)
        .build();
    }

    //cho admin
    @Order(2)
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .securityMatcher("/admin/**", "/adminlogin")
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/adminlogin").permitAll()
            .requestMatchers("/admin/**").hasAuthority("admin")
            .anyRequest().authenticated()
        ).formLogin(login->login 
            .loginPage("/adminlogin")
            .loginProcessingUrl("/adminlogin")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/admin/index",true)
        )
        .userDetailsService(customUserDetailService)
        .build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                        .debug(true)
                        .ignoring()
                        .requestMatchers("/assets/**");
    }
}
