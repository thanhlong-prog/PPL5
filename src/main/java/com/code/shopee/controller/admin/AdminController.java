package com.code.shopee.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.shopee.model.Cart;
import com.code.shopee.model.Product;
import com.code.shopee.model.Transaction;
import com.code.shopee.repository.CartRepository;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.repository.TransactionRepository;
import com.code.shopee.repository.UserRepository;
import com.code.shopee.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/index")
    public String Admin() {
        return "admin/index";
    }

    // @RequestMapping("")
    // public String redirectToAdminIndex() {
    // return "admin/dashboard";
    // }

    @RequestMapping("")
    public String home(Model model) {
        int totalPurchases = cartRepository.countByStatusTrueAndTransactionNotNull();
        model.addAttribute("totalPurchases", totalPurchases);

        List<Cart> carts = cartRepository.findByStatusTrueAndTransactionNotNull();
        int totalRevenue = carts.stream()
                .mapToInt(Cart::getTotalPrice)
                .sum();
        model.addAttribute("totalRevenue", totalRevenue);

        long totalUsers = userRepository.count();
        model.addAttribute("totalUsers", totalUsers);

        int totalProducts = productRepository.countByStatusTrue();
        model.addAttribute("totalProducts", totalProducts);
        return "admin/home";
    }

    @RequestMapping("/product")
    public String product(Model model) {
        List<Product> productsAll;
        List<Product> productsTrue;
        List<Product> productsFalse;
        List<Product> productsBaned;
        productsTrue = productRepository.findByStatusTrue();
        productsFalse = productRepository.findByStatusFalseAndIsBanFalse();
        productsAll = productRepository.findAll();
        productsBaned = productRepository.findByStatusFalseAndIsBanTrue();
        int productCountStatusTrue = productRepository.countByStatusTrue();
        int productCountStatusFalse = productRepository.countByStatusFalse();
        int productCountStatusBaned = productRepository.countByStatusFalseAndIsBanTrue();
        model.addAttribute("productCountStatusTrue", productCountStatusTrue);
        model.addAttribute("productCountStatusFalse", productCountStatusFalse);
        model.addAttribute("productsAll", productsAll);
        model.addAttribute("productsTrue", productsTrue);
        model.addAttribute("productsFalse", productsFalse);
        model.addAttribute("productsBaned", productsBaned);
        model.addAttribute("productCountStatusBaned", productCountStatusBaned);

        return "admin/product-manage";
    }

    @RequestMapping("/user")
    public String user(Model model) {
        int totalPurchases = cartRepository.countByStatusTrueAndTransactionNotNull();
        model.addAttribute("totalPurchases", totalPurchases);

        List<Cart> carts = cartRepository.findByStatusTrueAndTransactionNotNull();
        int totalRevenue = carts.stream()
                .mapToInt(Cart::getTotalPrice)
                .sum();
        model.addAttribute("totalRevenue", totalRevenue);

        long totalUsers = userRepository.count();
        model.addAttribute("totalUsers", totalUsers);

        int totalProducts = productRepository.countByStatusTrue();
        model.addAttribute("totalProducts", totalProducts);

        return "admin/user-manage";
    }

    @RequestMapping("/purchase")
    public String purchase(Model model) {
        List<Transaction> transactions = transactionRepository.findAllWithCarts();
        for (Transaction t : transactions) {
                int total = t.getCarts().stream().mapToInt(Cart::getTotalPrice).sum();
                t.setTotal(total);
            }
        model.addAttribute("transactions", transactions);
        return "admin/purchase-manage";
    }

    @RequestMapping("/dashboard")
    public String dashboardPage(Model model) {
        int totalPurchases = cartRepository.countByStatusTrueAndTransactionNotNull();
        model.addAttribute("totalPurchases", totalPurchases);

        List<Cart> carts = cartRepository.findByStatusTrueAndTransactionNotNull();
        int totalRevenue = carts.stream()
                .mapToInt(Cart::getTotalPrice)
                .sum();
        model.addAttribute("totalRevenue", totalRevenue);

        long totalUsers = userRepository.count();
        model.addAttribute("totalUsers", totalUsers);

        int totalProducts = productRepository.countByStatusTrue();
        model.addAttribute("totalProducts", totalProducts);
        return "admin/dashboard";
    }
}
