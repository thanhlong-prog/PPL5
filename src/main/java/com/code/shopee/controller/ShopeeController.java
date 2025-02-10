package com.code.shopee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ShopeeController {
    @GetMapping("login")
    public String Login() {
        return "buyer/login";
    }

    @GetMapping("register")
    public String Register() {
        return "buyer/register";
    }
}
