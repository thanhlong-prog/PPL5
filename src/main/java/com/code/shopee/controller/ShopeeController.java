package com.code.shopee.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.code.shopee.dto.MailDto;
import com.code.shopee.dto.UserDto;
import com.code.shopee.model.Category;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductImage;
import com.code.shopee.model.ProductOptionValues;
import com.code.shopee.model.ProductOptions;
import com.code.shopee.model.ProductPreview;
import com.code.shopee.model.ProductVatiants;
import com.code.shopee.model.Subcategory;
import com.code.shopee.model.User;
import com.code.shopee.repository.ProductPreviewRepo;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.repository.UserRepository;
import com.code.shopee.service.CategoryService;
import com.code.shopee.service.CloudinaryService;
import com.code.shopee.service.MailService;
import com.code.shopee.service.ProductImageService;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopeeController {
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductPreviewRepo productPreviewRepository;
    @Autowired
    private ProductRepository productRepo;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @RequestMapping("")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        List<Category> categories = categoryService.getAllCategoryStatusTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("numOfCateg", categories.size());
        Page<Product> products = productService.getAllProductStatusTrue(page);
        if (page > products.getTotalPages() && products.getTotalPages() != 0) {
            return "redirect:/home?page=" + products.getTotalPages();
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("numOfProducts", products.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        return "guest/index";
    }

    @RequestMapping("/product")
    public String product(@RequestParam(value = "productId") int productId, Model model) {
        Product product = productService.getProductByIdAndStatusTrue(productId);
        model.addAttribute("product", product);
        List<ProductImage> productImages = productImageService.getAllProductImageStatusTrue(productId);
        model.addAttribute("productImages", productImages);

        int totalQuantity = product.getQuantity();

        model.addAttribute("totalQuantity", totalQuantity);
        // System.err.println("color: " + colors);
        Map<ProductOptions, List<ProductOptionValues>> mapOptions = productService
                .getOptionWithValuesByStatusTrue(productId);
        model.addAttribute("options", mapOptions);
        List<ProductVatiants> productVatiants = productService.getAllProductVatiantsByStatusTrue(productId);
        Map<ProductVatiants, List<ProductOptionValues>> listVatians = new LinkedHashMap<>();
        for (ProductVatiants variant : productVatiants) {
            List<ProductOptionValues> values = new ArrayList<>(variant.getOptionValues());
            values.sort(Comparator.comparingInt(ProductOptionValues::getId));
            listVatians.put(variant, values);
        }
        model.addAttribute("listVatians", listVatians);

        List<ProductPreview> productPreviews = productPreviewRepository
                .findAllByProductIdAndStatusTrue(product.getId());
        int count5Star = 0;
        int count4Star = 0;
        int count3Star = 0;
        int count2Star = 0;
        int count1Star = 0;
        int totalReviews = productPreviews.size();
        for (ProductPreview preview : productPreviews) {
            switch (preview.getStar()) {
                case 5 -> count5Star++;
                case 4 -> count4Star++;
                case 3 -> count3Star++;
                case 2 -> count2Star++;
                case 1 -> count1Star++;
            }
        }

        int fullStars = (int) Math.floor(product.getRating());

        String stars = "";
        for (int i = 0; i < fullStars; i++) {
            stars += "★";
        }
        for (int i = fullStars; i < 5; i++) {
            stars += "☆";
        }

        model.addAttribute("stars", stars);
        model.addAttribute("count5Star", count5Star);
        model.addAttribute("count4Star", count4Star);
        model.addAttribute("count3Star", count3Star);
        model.addAttribute("count2Star", count2Star);
        model.addAttribute("count1Star", count1Star);
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("productPreviews", productPreviews);
        return "guest/product";
    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<?> updateQuantity(@RequestBody Map<String, Object> payload) {
        int productId = Integer.parseInt(payload.get("productId").toString());
        @SuppressWarnings("unchecked")
        Map<String, String> selectedOptions = (Map<String, String>) payload.get("selectedOptions");

        int quantity = productService.getQuantityByOptions(productId, selectedOptions);
        int price = productService.getPriceByOptions(productId, selectedOptions);

        return ResponseEntity.ok(Map.of(
                "quantity", quantity,
                "price", price));
    }

    @RequestMapping("/adminlogin")
    public String adminLogin() {
        return "admin/login";
    }

    @RequestMapping("/seller-manage")
    public String SellerManage() {
        return "seller/seller-manage";
    }

    @RequestMapping("/forgotPassword")
    public String forgotPassword() {

        return "buyer/forgot-password";
    }

    @RequestMapping("/list")
    public String search(@RequestParam(value = "key") String key, Model model) {
        

        List<Product> allProducts = productRepo.findAllByStatusTrueAndIsBanFalse();

        String keyLower = key.toLowerCase();
        StringMetric metric = StringMetrics.jaroWinkler();

        List<Product> matchedProducts = allProducts.stream()
                .filter(p -> {
                    String titleLower = p.getTitle().toLowerCase();

                    if (titleLower.contains(keyLower)) {
                        return true;
                    }

                    String[] keyWords = keyLower.split("\\s+");
                    String[] titleWords = titleLower.split("\\s+");

                    for (String kw : keyWords) {
                        for (String tw : titleWords) {
                            if (metric.compare(kw, tw) > 0.8) {
                                return true;
                            }
                        }
                    }

                    return false;
                })
                .collect(Collectors.toList());

        model.addAttribute("products", matchedProducts);
        model.addAttribute("keyword", key);

        return "guest/searchbyname";
    }

    @RequestMapping("/list-search")
    public String search(@RequestParam(value = "categoryId") int categoryId,
            @RequestParam(value = "subcategoryId") int subId, Model model) {

        Category category = categoryService.getCategoryByIdAndStatusTrue(categoryId);
        List<Subcategory> listSubs = category.getSubcategories();
        Set<Product> listProducts = new HashSet<>();
        if (subId == 0) {
            if (listSubs != null && !listSubs.isEmpty()) {
                listSubs.forEach(sub -> {
                    List<Product> products = productService.getProductBySubcateId(sub.getId());
                    if (products != null && !products.isEmpty()) {
                        listProducts.addAll(products);
                    }
                });
            }
        } else {
            List<Product> products = productService.getProductBySubcateId(subId);
            if (products != null && !products.isEmpty()) {
                listProducts.addAll(products);
            }
        }

        model.addAttribute("listPro", listProducts);
        model.addAttribute("category", category);
        return "guest/search";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam(value = "email") String email,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "new-password") String newPassword,
            @RequestParam(value = "confirm-password") String retypepass, Model model, HttpSession session) {
        if (email == null || code == null || newPassword == null || retypepass == null) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            return "buyer/forgot-password";
        }
        if (!checkMailExist(email)) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống");
            return "buyer/forgot-password";
        }
        if (!newPassword.equals(retypepass)) {
            model.addAttribute("error", "Mật khẩu không khớp");
            return "buyer/forgot-password";
        }
        String codeInSession = (String) session.getAttribute("codeMail");
        if (codeInSession == null || !codeInSession.equals(code)) {
            model.addAttribute("error", "Mã xác thực không đúng hoặc đã hết hạn");
            return "buyer/forgot-password";
        }

        User user = userService.findByGmail(email);

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        session.removeAttribute("codeMail");
        return "redirect:/login";
    }

    @RequestMapping("/admin-login")
    public String Chat() {
        return "admin/login";
    }

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
    // return "test/upload-img";
    // }
    // @PostMapping("/upload")
    // public String uploadFile(@RequestParam("image") MultipartFile file) {
    // try {
    // String imageUrl = cloudinaryService.getImageUrl(file);
    // System.out.println("Uploaded image URL: " + imageUrl);
    // } catch (Exception e) {
    // e.printStackTrace();
    // return "Error uploading file: " + e.getMessage();
    // }
    // return "Uploaded: " + file.getOriginalFilename();

    @PostMapping("/sendMail")
    @ResponseBody
    public ResponseEntity<?> sendMail(@RequestParam("email") String email, HttpSession session) {
        try {
            String userGmail = null;
            if (checkMailExist(email)) {
                String codeMail = randomCode();
                session.setAttribute("codeMail", codeMail);
                scheduler.schedule(() -> {
                    session.removeAttribute("codeMail");
                }, 300, TimeUnit.SECONDS);
                MailDto mess = new MailDto(email, "Mã xác thực gmail", codeMail);
                mailService.sendEmail(mess);
                return ResponseEntity.ok("success");
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email không tồn tại trong hệ thống");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email thất bại");
        }
    }

    public String randomCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomDigit = (int) (Math.random() * 10);
            code.append(randomDigit);
        }
        return code.toString();
    }

    public Boolean checkMailExist(String email) {
        User user = userService.findByGmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }
}
