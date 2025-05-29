package com.code.shopee.controller.buyer.home;

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
import com.code.shopee.model.User;
import com.code.shopee.repository.SubcategoryRepo;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;

@Controller
@RequestMapping("/buyer/shop")
public class ShopHomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private SubcategoryRepo subcategoryRepository;

    @RequestMapping("")
    public String shopHome(@RequestParam(value = "shopId") int shopid, Model model) {
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

        User seller = userService.findById(shopid);
        if (seller == null) {
            return "redirect:/home";
        }
        UserDto shopData = userMapper.toUserDto(seller);
        model.addAttribute("shop", shopData);
        List<Product> products = productService.getProductBySeller(shopid);
        model.addAttribute("products", products);
        // Map<Category, List<Product>> productsByCategory = products.stream()
        //         .filter(p -> p.getSubcategory() != null && p.getSubcategory().getCategory() != null)
        //         .collect(Collectors.groupingBy(p -> p.getSubcategory().getCategory()));

        // model.addAttribute("productsByCategory", productsByCategory);
        return "shop/shop-home";
    }
}
