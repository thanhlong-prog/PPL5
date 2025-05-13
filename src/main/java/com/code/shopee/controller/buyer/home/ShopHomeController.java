package com.code.shopee.controller.buyer.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.User;
import com.code.shopee.service.UserService;

@Controller
@RequestMapping("/buyer/shop")
public class ShopHomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("")
    public String shopHome(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            consumer = userService.findByGmail(email);
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        return "shop/shop-home";
    }
}
