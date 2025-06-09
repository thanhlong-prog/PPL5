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
    @RequestMapping("/seller-manage")
    public String SellerManage() {
        return "seller/seller-manage";
    }
    // @RequestMapping("/chat")
    // public String Chat() {
    //     return "chat/chat";
    // }
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


