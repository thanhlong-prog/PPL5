package com.code.shopee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ShopeeController {
    @RequestMapping("/login")
    public String Login() {
        return "buyer/login";
    }

    @RequestMapping("/register")
    public String Register() {
        return "buyer/register";
    }
    @RequestMapping("/")
    public String Home() {
        return "home/index";
    }
    @RequestMapping("/adminlogin")
    public String adminLogin() {
        return "admin/login";
    }
}
