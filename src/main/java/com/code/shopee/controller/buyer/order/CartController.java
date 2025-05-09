package com.code.shopee.controller.buyer.order;

import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.Cart;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.User;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/buyer/cart")
public class CartController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductService productService;

    @RequestMapping("")
    public String cart(@RequestParam(value="id") int id, Model model) {
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
        List<Cart> cartList = productService.getCartByUserIdAndStatusTrue(consumer.getId());
        Collections.reverse(cartList);
        model.addAttribute("cartList", cartList);
        model.addAttribute("checkedId", id);
        return "home/cart";
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> addCart(@RequestParam("cartId") int cartId, HttpSession session) {
        try {
            productService.deleteCart(cartId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không xóa được sản phẩm khỏi giỏ hàng");
        }
    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<?> updateQuantity(@RequestParam("cartId") int cartId,
            @RequestParam("quantity") int quantity, HttpSession session) {
        try {
            Cart cart = productService.getCartByIdAndStatusTrue(cartId);
            if (cart == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy sản phẩm trong giỏ hàng");
            }
            if (quantity < 1) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Số lượng sản phẩm không hợp lệ");
            }
            if (quantity > cart.getProductOption().getQuantity()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Số lượng sản phẩm không đủ trong kho");
            }
            cart.setOrderQuantity(quantity);
            productService.addCart(cart);
            Map<String, Integer> response = new HashMap<>();
            double totalPrice = 0;
            if(cart.getProduct().getDiscount() != null) {
                totalPrice = cart.getProductOption().getPrice() * cart.getProduct().getDiscount()/100  * quantity;
            } else {
                totalPrice = cart.getProductOption().getPrice() * quantity;
            }
            
            response.put("totalPrice", (int) totalPrice);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không cập nhật được số lượng sản phẩm trong giỏ hàng");
        }
    }
}
