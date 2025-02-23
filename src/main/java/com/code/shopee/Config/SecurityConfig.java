package com.code.shopee.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/*").permitAll()
            .requestMatchers("/admin/**").hasAuthority("admin")
            .anyRequest().authenticated()
        ).formLogin(login->login 
            .loginPage("/adminlogin")
            .loginProcessingUrl("/adminlogin")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/admin/index",true)
        ).userDetailsService(customUserDetailService)
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
