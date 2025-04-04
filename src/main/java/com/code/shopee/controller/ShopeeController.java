package com.code.shopee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ShopeeController {

    @RequestMapping("/adminlogin")
    public String adminLogin() {
        return "admin/login";
    }
    @RequestMapping("/cart")
    public String Cart() {
        return "home/cart";
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
}
