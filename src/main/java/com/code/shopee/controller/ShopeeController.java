package com.code.shopee.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.code.shopee.dto.MailDto;
import com.code.shopee.model.User;
import com.code.shopee.repository.UserRepository;
import com.code.shopee.service.CloudinaryService;
import com.code.shopee.service.MailService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopeeController {
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserRepository userRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @RequestMapping("/adminlogin")
    public String adminLogin() {
        return "admin/login";
    }

    @RequestMapping("/seller-manage")
    public String SellerManage() {
        return "seller/seller-manage";
    }

    @RequestMapping("/forgotPassword")
    public String forgotPassword() {

        return "buyer/forgot-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam(value = "email") String email,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "new-password") String newPassword,
            @RequestParam(value = "confirm-password") String retypepass, Model model, HttpSession session) {
        if (email == null || code == null || newPassword == null || retypepass == null) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            return "buyer/forgot-password";
        }
        if (!checkMailExist(email)) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống");
            return "buyer/forgot-password";
        }
        if (!newPassword.equals(retypepass)) {
            model.addAttribute("error", "Mật khẩu không khớp");
            return "buyer/forgot-password";
        }
        String codeInSession = (String) session.getAttribute("codeMail");
        if (codeInSession == null || !codeInSession.equals(code)) {
            model.addAttribute("error", "Mã xác thực không đúng hoặc đã hết hạn");
            return "buyer/forgot-password";
        }

        User user = userService.findByGmail(email);

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        session.removeAttribute("codeMail");
        return "redirect:/login";
    }

    @RequestMapping("/admin-login")
    public String Chat() {
        return "admin/login";
    }

    @RequestMapping("product-manage")
    public String bulk() {
        return "seller/product-manage";
    }

    @RequestMapping("/checkout")
    public String Checkout() {
        return "checkout/checkout";
    }

    @RequestMapping("/shop-home")
    public String Search() {
        return "shop/shop-home";
    }

    @RequestMapping("/seller-register")
    public String SellerRegister() {
        return "seller/seller-register";
    }

    @RequestMapping("/seller-add-products")
    public String SellerAddProducts() {
        return "seller/seller-add-products";
    }

    @RequestMapping("/info")
    public String Info() {
        return "buyer/info-form";
    }

    @RequestMapping("/terms-of-service")
    public String termsOfService() {
        return "terms-of-service";
    }

    @RequestMapping("/privacy-policy")
    public String privacyPolicy() {
        return "privacy-policy";
    }
    // @RequestMapping("/upload")
    // public String about() {
    // return "test/upload-img";
    // }
    // @PostMapping("/upload")
    // public String uploadFile(@RequestParam("image") MultipartFile file) {
    // try {
    // String imageUrl = cloudinaryService.getImageUrl(file);
    // System.out.println("Uploaded image URL: " + imageUrl);
    // } catch (Exception e) {
    // e.printStackTrace();
    // return "Error uploading file: " + e.getMessage();
    // }
    // return "Uploaded: " + file.getOriginalFilename();

    @PostMapping("/sendMail")
    @ResponseBody
    public ResponseEntity<?> sendMail(@RequestParam("email") String email, HttpSession session) {
        try {
            String userGmail = null;
            if (checkMailExist(email)) {
                String codeMail = randomCode();
                session.setAttribute("codeMail", codeMail);
                scheduler.schedule(() -> {
                    session.removeAttribute("codeMail");
                }, 300, TimeUnit.SECONDS);
                MailDto mess = new MailDto(email, "Mã xác thực gmail", codeMail);
                mailService.sendEmail(mess);
                return ResponseEntity.ok("success");
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email không tồn tại trong hệ thống");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email thất bại");
        }
    }

    public String randomCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomDigit = (int) (Math.random() * 10);
            code.append(randomDigit);
        }
        return code.toString();
    }

    public Boolean checkMailExist(String email) {
        User user = userService.findByGmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }
}
