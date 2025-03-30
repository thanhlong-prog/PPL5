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
}
