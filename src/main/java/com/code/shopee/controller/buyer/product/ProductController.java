package com.code.shopee.controller.buyer.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductImage;
import com.code.shopee.model.User;
import com.code.shopee.service.ProductImageService;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;


@Controller
@RequestMapping("/buyer/product")
public class ProductController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;

    @RequestMapping("")
    public String product(@RequestParam(value = "productId") int productId, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            consumer = userService.findByGmail(email);  
        }
        Product product = productService.getProductByIdAndStatusTrue(productId);
        model.addAttribute("product", product);
        List<ProductImage> productImages = productImageService.getAllProductImageStatusTrue(productId);
        model.addAttribute("productImages", productImages);
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        return "product/product";
    }
}
