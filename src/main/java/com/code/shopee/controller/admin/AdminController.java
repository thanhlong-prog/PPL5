package com.code.shopee.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.code.shopee.dto.banRequestDto;
import com.code.shopee.dto.unBanRequestDto;
import com.code.shopee.model.BanInfo;
import com.code.shopee.model.Cart;
import com.code.shopee.model.Product;
import com.code.shopee.model.SellerInfo;
import com.code.shopee.model.Transaction;
import com.code.shopee.model.User;
import com.code.shopee.repository.BanInfoRepo;
import com.code.shopee.repository.CartRepository;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.repository.SellerInfoRepo;
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
    @Autowired
    private SellerInfoRepo sellerInfoRepo;
    @Autowired
    private BanInfoRepo banInfoRepo;
    @Autowired
    private CartRepository cartRepo;

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
        List<User> activeUsers = userRepository.findActiveEnabledBuyers();
        model.addAttribute("activeUsers", activeUsers);

        List<User> activeSellerUsers = userRepository.findActiveEnabledSellersWithProducts();

        for (User user : activeSellerUsers) {
            int total = 0;
            for (Product product : user.getProducts()) {
                total += product.getCarts().stream()
                        .filter(cart -> cart.getTransaction() != null
                                && cart.getStatus() == 1
                                && cart.getShippingStatus() == 1)
                        .mapToInt(Cart::getTotalPrice)
                        .sum();
            }
            user.setRevenue(total);
        }

        model.addAttribute("activeSellerUsers", activeSellerUsers);

        List<BanInfo> bannedUsers = banInfoRepo.findAllByStatusTrue();
        model.addAttribute("bannedUsers", bannedUsers);

        List<SellerInfo> sellerInfo = sellerInfoRepo.findAll();
        model.addAttribute("sellerInfo", sellerInfo);

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

    @PostMapping("/getUserDetail")
    @ResponseBody
    public ResponseEntity<?> getUserDetail(@RequestBody int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        String name = user.getName();
        String email = user.getGmail();
        String phone = user.getPhone();
        return ResponseEntity.ok(Map.of(
                "name", name,
                "email", email,
                "phone", phone));
    }

    @PostMapping("/approve")
    @ResponseBody
    public ResponseEntity<?> approve(@RequestBody List<Integer> ids) {
        for (int id : ids) {
            Product Product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
            Product.setStatus(true);
            Product.setBan(false);
            Product.setModifiedDate(LocalDate.now());
            productRepository.save(Product);
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/banProduct")
    @ResponseBody
    public ResponseEntity<?> banProduct(@RequestBody List<Integer> ids) {
        for (int id : ids) {
            Product Product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
            Product.setStatus(false);
            Product.setBan(true);
            Product.setModifiedDate(LocalDate.now());
            productRepository.save(Product);
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/unbanProduct")
    @ResponseBody
    public ResponseEntity<?> unbanProduct(@RequestBody List<Integer> ids) {
        for (int id : ids) {
            Product Product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
            Product.setStatus(true);
            Product.setBan(false);
            Product.setModifiedDate(LocalDate.now());
            productRepository.save(Product);
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/letBecomeSeller")
    @ResponseBody
    public ResponseEntity<?> letBecomeSeller(@RequestBody int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        SellerInfo sellerInfo = user.getSellerInfo();
        sellerInfo.setVerify(true);
        sellerInfo.setStatus(1);
        sellerInfo.setModifiedDate(LocalDateTime.now());
        sellerInfoRepo.save(sellerInfo);

        user.setBecomeSellerDate(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/refuseBecomeSeller")
    @ResponseBody
    public ResponseEntity<?> refuseBecomeSeller(@RequestBody int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        SellerInfo sellerInfo = user.getSellerInfo();
        sellerInfo.setVerify(false);
        sellerInfo.setStatus(0);
        sellerInfo.setModifiedDate(LocalDateTime.now());
        sellerInfoRepo.save(sellerInfo);

        user.setBecomeSellerDate(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/banUser")
    @ResponseBody
    public ResponseEntity<?> banUser(@RequestBody banRequestDto request) {
        int userId = request.getUserId();
        String banType = request.getBanType();
        String reason = request.getReason();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        BanInfo banInfo = new BanInfo();

        if ("buyer".equals(banType)) {
            user.setEnable(false);
            user.setStatus(false);
            userRepository.save(user);

            banInfo.setUser(user);
            banInfo.setReason(reason);
            banInfo.setBanType("buyer");
            banInfo.setBanDate(LocalDateTime.now());
            banInfo.setStatus(true);
            banInfoRepo.save(banInfo);

        } else if ("seller".equals(banType)) {
            user.getSellerInfo().setVerify(false);
            sellerInfoRepo.save(user.getSellerInfo());

            user.getProducts().forEach(product -> {
                product.setStatus(false);
                product.setBan(true);
                productRepository.save(product);
            });

            banInfo.setUser(user);
            banInfo.setReason(reason);
            banInfo.setBanType("seller");
            banInfo.setBanDate(LocalDateTime.now());
            banInfo.setStatus(true);
            banInfoRepo.save(banInfo);
        } else {
            return ResponseEntity.badRequest().body("Invalid ban type");
        }

        return ResponseEntity.ok("success");
    }

    @PostMapping("/unBanUser")
    @ResponseBody
    public ResponseEntity<?> unBanUser(@RequestBody unBanRequestDto request) {
        int userId = request.getUserId();
        String banType = request.getBanType();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        BanInfo banInfo = banInfoRepo.findByUserIdAndStatusTrueAndBanType(userId, banType);

        if ("buyer".equals(banType)) {
            user.setEnable(true);
            user.setStatus(true);
            userRepository.save(user);

            banInfo.setUnbanDate(LocalDateTime.now());
            banInfo.setStatus(false);
            banInfoRepo.save(banInfo);

        } else if ("seller".equals(banType)) {
            user.getSellerInfo().setVerify(true);
            sellerInfoRepo.save(user.getSellerInfo());

            banInfo.setUnbanDate(LocalDateTime.now());
            banInfo.setStatus(false);
            banInfoRepo.save(banInfo);
        } else {
            return ResponseEntity.badRequest().body("Invalid ban type");
        }

        return ResponseEntity.ok("success");
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

        int productCountStatusTrue = productRepository.countByStatusTrue();
        int productCountStatusFalse = productRepository.countByStatusFalse();
        int productCountStatusBaned = productRepository.countByStatusFalseAndIsBanTrue();
        model.addAttribute("productCountStatusTrue", productCountStatusTrue);
        model.addAttribute("productCountStatusFalse", productCountStatusFalse);
        model.addAttribute("productCountStatusBaned", productCountStatusBaned);

        return "admin/dashboard";
    }

    @PostMapping("/get-revenue-year")
    @ResponseBody
    public ResponseEntity<?> getRevenueByYear(@RequestBody Map<String, String> body) {
        int year = Integer.parseInt(body.get("year"));

        Map<Integer, Integer> revenueByMonth = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) {
            revenueByMonth.put(month, 0);
        }

        LocalDateTime startDateTime = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);

        List<Cart> carts = cartRepo.findByTransactionCreatedDateBetween(startDateTime, endDateTime);

        for (Cart cart : carts) {
            if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1) {
                int month = cart.getTransaction().getCreatedDate().getMonthValue();
                revenueByMonth.put(month, revenueByMonth.get(month) + cart.getTotalPrice());
            }
        }
        return ResponseEntity.ok(new ArrayList<>(revenueByMonth.values()));
    }
}
