package com.code.shopee.controller.buyer.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.Category;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Product;
import com.code.shopee.model.User;
import com.code.shopee.service.CategoryService;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;


@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @RequestMapping("")
    public String User(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
            if(!user.isVerify()) {
                return "redirect:/buyer/verify";
            }
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            consumer = userService.findByGmail(email);  
            if (!consumer.getVerify()) {
                return "redirect:/buyer/verify"; 
            }
        }
        List<Category> categories = categoryService.getAllCategoryStatusTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("numOfCateg", categories.size());
        Page<Product> products = productService.getAllProductStatusTrue(page);
        if(page > products.getTotalPages() && products.getTotalPages() != 0) {
            return "redirect:/home?page=" + products.getTotalPages();
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("numOfProducts", products.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        return "home/index";
    }
}
