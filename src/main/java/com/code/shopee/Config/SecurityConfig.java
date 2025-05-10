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

import com.code.shopee.service.authen.CustomUserDetailService;

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
        .securityMatcher("/buyer/**", "/login", "/register", "/logout", "/oauth2/**", "/login/**", "/home/**", "/api/**")
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/register", "/oauth2/**", "/login/**").permitAll()
            .requestMatchers("/buyer/**").hasAuthority("buyer")
            .requestMatchers("/home/**").hasAuthority("buyer")
            .anyRequest().authenticated()
        ).formLogin(login->login 
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/home",true)
        ).oauth2Login(oauth2 -> oauth2 
            .loginPage("/login")
            .defaultSuccessUrl("/home", true)
        ).logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
        )
        .userDetailsService(customUserDetailService)
        .build();
    }

    //cho admin
    @Order(2)
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .securityMatcher("/admin/**", "/adminlogin", "/adminlogout")
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
        ).logout(logout -> logout
            .logoutUrl("/adminlogout")
            .logoutSuccessUrl("/adminlogin")
        )
        .userDetailsService(customUserDetailService)
        .build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                        .debug(false)
                        .ignoring()
                        .requestMatchers("/assets/**", "/terms-of-service", "/privacy-policy");
    }
}
