package com.code.shopee.controller.seller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller/purchase")
public class PurchaseController {
    @RequestMapping("/manage")
    public String managePurchases() {
        // return "seller/cancel-order";
        // return "seller/purchase-manage";
        return  "seller/order-manage";  
    }
}
