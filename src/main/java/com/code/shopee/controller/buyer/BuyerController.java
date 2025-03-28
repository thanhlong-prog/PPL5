package com.code.shopee.controller.buyer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/buyer")
public class BuyerController {
    @RequestMapping("/home")
    public String User() {
        return "home/index";
    }
}
