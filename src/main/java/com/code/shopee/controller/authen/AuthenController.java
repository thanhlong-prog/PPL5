package com.code.shopee.controller.authen;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.enums.Role;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Roles;
import com.code.shopee.service.RolesService;
import com.code.shopee.service.UserService;

import jakarta.validation.Valid;



@Controller
public class AuthenController {

    @Autowired 
    private UserService userService;
    @Autowired
    private RolesService rolesService;


    @RequestMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("buyer"))) {
                return "redirect:/home";
            }
        } else if (principal instanceof OAuth2User oauth2User) {
            if(oauth2User.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("buyer"))) {
                return "redirect:/home";
            };
        }
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
        return "buyer/login";
    }

    @RequestMapping("/register")
    public String registerPage(@RequestParam(value = "error", required = false) String error, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("buyer"))) {
                return "redirect:/home";
            }
        } else if (principal instanceof OAuth2User oauth2User) {
            if(oauth2User.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("buyer"))) {
                return "redirect:/home";
            };
        }
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
        }
        return "buyer/register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("RegistrationDto") RegistrationDto user, BindingResult result) {
        
        if (result.hasErrors()) {
            return "redirect:/register?error=true";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            return "redirect:/register?error=true";
        }
        Set<Roles> roles = new HashSet<>();
        roles.add(rolesService.findById(Role.BUYER.getCode()));
        user.setRoles(roles);
        user.setAvatar("https://res.cloudinary.com/dowc3tobj/image/upload/v1744467877/Image/zaqfhfcloizdf83uu8gl.jpg");
        user.setVerify(false);
        user.setEnable(true);
        user.setStatus(true);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }
    
}
