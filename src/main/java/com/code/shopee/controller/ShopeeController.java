package com.code.shopee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.shopee.service.CloudinaryService;


@Controller
public class ShopeeController {
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @RequestMapping("/adminlogin")
    public String adminLogin() {
        return "admin/login";
    }
    // @RequestMapping("/cart")
    // public String Cart() {
    //     return "home/cart";
    // }
    @RequestMapping("/checkout")
    public String Checkout() {
        return "checkout/checkout";
    }
    @RequestMapping("/search")
    public String Search() {
        return "home/search";
    }
    @RequestMapping("/seller-register")
    public String SellerRegister() {
        return "seller/seller-register";
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
    //     return "test/upload-img";
    // }
    // @PostMapping("/upload")
    // public String uploadFile(@RequestParam("image") MultipartFile file) {
    // try {
    //     String imageUrl = cloudinaryService.getImageUrl(file);
    //     System.out.println("Uploaded image URL: " + imageUrl);
    // } catch (Exception e) {
    //     e.printStackTrace();
    //     return "Error uploading file: " + e.getMessage();
    // }
    // return "Uploaded: " + file.getOriginalFilename();
}


