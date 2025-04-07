package com.code.shopee.controller.buyer.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.User;
import com.code.shopee.service.UserService;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserService userService;

    @RequestMapping("")
    public String User() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            if(!user.isVerify()) {
                return "redirect:/buyer/verify";
            }
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            User user = userService.findByGmail(email);  
            if (!user.getVerify()) {
                return "redirect:/buyer/verify"; 
            }
        }
        return "home/index";
    }
}
