package com.code.shopee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ShopeeController {
    @GetMapping("hello")
    public String Hello() {
        return "admin/index";
    }
}
