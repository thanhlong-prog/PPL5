package com.code.shopee.controller.buyer.product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.Cart;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductImage;
import com.code.shopee.model.ProductOptionValues;
import com.code.shopee.model.ProductOptions;
import com.code.shopee.model.ProductVatiants;
import com.code.shopee.model.User;
import com.code.shopee.service.ProductImageService;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpSession;

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

        int totalQuantity = product.getQuantity();

        model.addAttribute("totalQuantity", totalQuantity);
        // System.err.println("color: " + colors);
        Map<ProductOptions, List<ProductOptionValues>> mapOptions = productService
                .getOptionWithValuesByStatusTrue(productId);
        model.addAttribute("options", mapOptions);
        List<ProductVatiants> productVatiants = productService.getAllProductVatiantsByStatusTrue(productId);
        Map<ProductVatiants, List<ProductOptionValues>> listVatians = new LinkedHashMap<>();
        for (ProductVatiants variant : productVatiants) {
            List<ProductOptionValues> values = new ArrayList<>(variant.getOptionValues());
            values.sort(Comparator.comparingInt(ProductOptionValues::getId));
            listVatians.put(variant, values);
        }
        model.addAttribute("listVatians", listVatians);
        return "product/product";
    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<?> updateQuantity(@RequestBody Map<String, Object> payload) {
        int productId = Integer.parseInt(payload.get("productId").toString());
        @SuppressWarnings("unchecked")
        Map<String, String> selectedOptions = (Map<String, String>) payload.get("selectedOptions");

        int quantity = productService.getQuantityByOptions(productId, selectedOptions);

        return ResponseEntity.ok(Map.of("quantity", quantity));
    }

    @PostMapping("/addcart")
    @ResponseBody
    public ResponseEntity<?> addCart(@RequestBody Map<String, Object> payload, HttpSession session) {
        try {
            int productId = Integer.parseInt(payload.get("productId").toString());
            int orderQuantity = Integer.parseInt(payload.get("orderQuantity").toString());

            if (orderQuantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số lượng không hợp lệ");
            }
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User consumer = new User();
            if (principal instanceof CustomUserDetails user) {
                consumer = userService.findByUsername(user.getUsername());
            } else if (principal instanceof OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                consumer = userService.findByGmail(email);
            }
            @SuppressWarnings("unchecked")
            Map<String, String> selectedOptions = (Map<String, String>) payload.get("selectedOptions");
            ProductVatiants variant = productService.getVatiantByOptions(productId, selectedOptions);
            if (variant == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Không tìm thấy phiên bản sản phẩm tương ứng");
            }

            Cart cart = new Cart();
            cart.setStatus(1);
            cart.setOrderQuantity(orderQuantity);
            cart.setUser(consumer);
            cart.setProduct(variant.getProduct());
            cart.setProductVatiants(variant); 
            cart.setCreatedDate(LocalDate.now());
            cart.setModifiedDate(LocalDate.now());
            productService.addCart(cart);
            return ResponseEntity.ok(cart.getId());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thêm vào được giỏ hàng!");
        }
    }
}
