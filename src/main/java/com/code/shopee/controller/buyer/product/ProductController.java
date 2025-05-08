package com.code.shopee.controller.buyer.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.Cart;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductImage;
import com.code.shopee.model.ProductOption;
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
        List<ProductOption> productOptions = productService.getProductOptionByStatusTrue(productId);

        Set<String> versions = productOptions.stream()
                .map(ProductOption::getVersion)
                .filter(version -> version != null && !version.isEmpty())
                .collect(Collectors.toSet());

        Set<String> colors = productOptions.stream()
                .map(ProductOption::getColor)
                .filter(color -> color != null && !color.isEmpty())
                .collect(Collectors.toSet());

        Set<String> sizes = productOptions.stream()
                .map(ProductOption::getSize)
                .filter(size -> size != null && !size.isEmpty())
                .collect(Collectors.toSet());

        int totalQuantity = product.getQuantity();
        model.addAttribute("versions", versions);
        model.addAttribute("colors", colors);
        model.addAttribute("sizes", sizes);
        model.addAttribute("listversionSize", versions.size());
        model.addAttribute("listcolorSize", colors.size());
        model.addAttribute("listsizeSize", sizes.size());
        model.addAttribute("totalQuantity", totalQuantity);
        System.err.println("color: " + colors);
        return "product/product";
    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<?> updateQuantity(@RequestParam("version") String version,
            @RequestParam("color") String color,
            @RequestParam("size") String size, @RequestParam("productId") int productId, HttpSession session) {
        try {
            List<ProductOption> productOptions = productService.getProductOptionByStatusTrue(productId);
            for (ProductOption productOption : productOptions) {

                if (version.equals(productOption.getVersion())
                        && color.equals(productOption.getColor())
                        && size.equals(productOption.getSize()) && productOption.getProduct().getId() == productId) {

                    Map<String, Integer> response = new HashMap<>();
                    response.put("quantity", productOption.getQuantity());
                    return ResponseEntity.ok(response);

                }

            }
            Map<String, Integer> response = new HashMap<>();
            response.put("quantity", 0);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update failed: " + e.getMessage());
        }
    }

    @PostMapping("/addcart")
    @ResponseBody
    public ResponseEntity<?> addCart(@RequestParam("orderQuantity") int orderQuantity,
            @RequestParam("productId") int productId,
            @RequestParam("version") String version,
            @RequestParam("color") String color,
            @RequestParam("size") String size, HttpSession session) {
        try {
            if (orderQuantity <= 0) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Số lượng không hợp lệ");
            }
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User consumer = new User();
            if (principal instanceof CustomUserDetails user) {
                consumer = userService.findByUsername(user.getUsername());
            } else if (principal instanceof OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                consumer = userService.findByGmail(email);
            }
            List<ProductOption> productOptions = productService.getProductOptionByStatusTrue(productId);
            for (ProductOption productOption : productOptions) {

                if (version.equals(productOption.getVersion())
                        && color.equals(productOption.getColor())
                        && size.equals(productOption.getSize()) && productOption.getProduct().getId() == productId) {
                    Cart cart = new Cart();
                    cart.setStatus(1);
                    cart.setOrderQuantity(orderQuantity);
                    cart.setProduct(productOption.getProduct());
                    cart.setProductOption(productOption);
                    cart.setUser(consumer);
                    cart.setCreatedDate(java.time.LocalDate.now());
                    cart.setModifiedDate(java.time.LocalDate.now());
                    productService.addCart(cart);
                    int cartId = cart.getId();
                    return ResponseEntity.ok(cartId);

                }
            }
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thêm vào được giỏ hàng!");
        }
    }
}
