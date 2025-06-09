package com.code.shopee.controller.buyer.product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.code.shopee.model.Subcategory;
import com.code.shopee.model.User;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.service.CategoryService;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;

@Controller
@RequestMapping("/buyer/search")
public class SearchController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepo;

    @RequestMapping("")
    public String search(@RequestParam(value = "categoryId") int categoryId,
            @RequestParam(value = "subcategoryId") int subId, Model model) {
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
        Category category = categoryService.getCategoryByIdAndStatusTrue(categoryId);
        List<Subcategory> listSubs = category.getSubcategories();
        Set<Product> listProducts = new HashSet<>();
        if (subId == 0) {
            if (listSubs != null && !listSubs.isEmpty()) {
                listSubs.forEach(sub -> {
                    List<Product> products = productService.getProductBySubcateId(sub.getId());
                    if (products != null && !products.isEmpty()) {
                        listProducts.addAll(products);
                    }
                });
            }
        } else {
            List<Product> products = productService.getProductBySubcateId(subId);
            if (products != null && !products.isEmpty()) {
                listProducts.addAll(products);
            }
        }

        model.addAttribute("listPro", listProducts);
        model.addAttribute("category", category);
        return "home/search";
    }

    @RequestMapping("/list")
    public String search(@RequestParam(value = "key") String key, Model model) {
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

        List<Product> allProducts = productRepo.findAllByStatusTrueAndIsBanFalse();

        String keyLower = key.toLowerCase();
        StringMetric metric = StringMetrics.jaroWinkler();

        List<Product> matchedProducts = allProducts.stream()
                .filter(p -> {
                    String titleLower = p.getTitle().toLowerCase();

                    if (titleLower.contains(keyLower)) {
                        return true;
                    }

                    String[] keyWords = keyLower.split("\\s+");
                    String[] titleWords = titleLower.split("\\s+");

                    for (String kw : keyWords) {
                        for (String tw : titleWords) {
                            if (metric.compare(kw, tw) > 0.8) {
                                return true;
                            }
                        }
                    }

                    return false;
                })
                .collect(Collectors.toList());

        model.addAttribute("products", matchedProducts);
        model.addAttribute("keyword", key);

        return "home/searchbyname";
    }

}
