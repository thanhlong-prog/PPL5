package com.code.shopee.service.authen;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.code.shopee.enums.Role;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Roles;
import com.code.shopee.model.User;
import com.code.shopee.request.FacebookUserRequest;
import com.code.shopee.request.GoogleUserRequest;
import com.code.shopee.service.RolesService;
import com.code.shopee.service.UserService;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private UserService userService;
    @Autowired
    private RolesService rolesService;
    
    private static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = null;

        if ("google".equals(registrationId)) {
            response = restTemplate.exchange(
                    GOOGLE_USER_INFO_URL,
                    HttpMethod.GET,
                    new org.springframework.http.HttpEntity<>(headers),
                    GoogleUserRequest.class
            );
            GoogleUserRequest googleUser = (GoogleUserRequest) response.getBody();
            if (googleUser == null) {
                throw new IllegalStateException("Failed to fetch user details from Google");
            }
            System.out.println("Google User Info: " + googleUser);
            
            String gmail = googleUser.getEmail();
            User user = userService.findByGmail(gmail);
            
            if (user == null) {
                user = new User();
                user.setAvatar(googleUser.getPicture());
                user.setGmail(gmail);
                user.setUsername(gmail);
                user.setName(googleUser.getName());
                Set<Roles> roles = new HashSet<>();
                roles.add(rolesService.findById(Role.BUYER.getCode()));  
                user.setRoles(roles);
                user.setVerify(false);
                user.setEnable(true);
                user.setStatus(true);
                userService.save(user);
            } else {
                Set<Roles> roles = user.getRoles();
                Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
                for (Roles role : roles) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
                }
                return new DefaultOAuth2User(grantedAuthorities, googleUser.getAttributes(), "sub");
            }
            return new CustomUserDetails(user, Collections.singletonList(new SimpleGrantedAuthority("buyer")), googleUser.getAttributes());
        } 
        else if ("facebook".equals(registrationId)) {
            response = restTemplate.exchange(
                    "https://graph.facebook.com/v12.0/me?fields=id,name,email,picture",
                    HttpMethod.GET,
                    new org.springframework.http.HttpEntity<>(headers),
                    FacebookUserRequest.class
            );

            FacebookUserRequest facebookUser = (FacebookUserRequest) response.getBody();
            if (facebookUser == null) {
                throw new IllegalStateException("Failed to fetch user details from Facebook");
            }
            System.out.println("Facebook User Info: " + facebookUser);
            
            String facebookId = facebookUser.getId();
            String gmail;
            if(facebookUser.getEmail() == null) {
                gmail = "example@gmail.com";
                facebookUser.setEmail(gmail);
            }
            else {
                gmail = facebookUser.getEmail();
            }
            User user = userService.findByFacebook(facebookId);  
            
            if (user == null) {
                user = new User();
                user.setAvatar("../images/img-avatar/avatar-custom.jpg");
                user.setUsername("user" + facebookId);
                user.setName(facebookUser.getName());
                user.setFacebook(facebookId);
                user.setGmail(gmail);
                Set<Roles> roles = new HashSet<>();
                roles.add(rolesService.findById(Role.BUYER.getCode()));  
                user.setRoles(roles);
                user.setVerify(false);
                user.setEnable(true);
                user.setStatus(true);
                userService.save(user);
            } else {
                Set<Roles> roles = user.getRoles();
                Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
                for (Roles role : roles) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
                }
                return new DefaultOAuth2User(grantedAuthorities, facebookUser.getAttributes(), "id");
            }
            return new CustomUserDetails(user, Collections.singletonList(new SimpleGrantedAuthority("buyer")), facebookUser.getAttributes());
        }
        throw new IllegalStateException("Unsupported OAuth2 provider");
    }

    public void checkGmailExist(String gmail) {
        User user = userService.findByGmail(gmail);
        if (user != null) {
            throw new IllegalStateException("Gmail already exists!");
        }
    }
}

