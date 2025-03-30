package com.code.shopee.controller.authen;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.enums.Role;
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
    public String loginPage() {
        return "buyer/login";
    }

    @RequestMapping("/register")
    public String registerPage() {
        return "buyer/register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDto user, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "buyer/register";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
            return "buyer/register";
        }
        Set<Roles> roles = new HashSet<>();
        roles.add(rolesService.findById(Role.BUYER.getCode()));
        user.setRoles(roles);
        user.setEnable(true);
        user.setStatus(true);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }
    
}
