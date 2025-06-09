package com.code.shopee.controller.buyer.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.Cart;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.PreviewImage;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductImage;
import com.code.shopee.model.ProductOptionValues;
import com.code.shopee.model.ProductOptions;
import com.code.shopee.model.ProductPreview;
import com.code.shopee.model.ProductVatiants;
import com.code.shopee.model.User;
import com.code.shopee.repository.CartRepository;
import com.code.shopee.repository.PreviewImageRepo;
import com.code.shopee.repository.ProductPreviewRepo;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.service.CloudinaryService;
import com.code.shopee.service.ProductImageService;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/buyer/product")
public class ProductController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ProductPreviewRepo productPreviewRepository;
    @Autowired
    private PreviewImageRepo previewImageRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

    @RequestMapping("")
    public String product(@RequestParam(value = "productId") int productId, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            consumer = userService.findByGmail(email);
        }
        Product product = productService.getProductByIdAndStatusTrue(productId);
        model.addAttribute("product", product);
        List<ProductImage> productImages = productImageService.getAllProductImageStatusTrue(productId);
        model.addAttribute("productImages", productImages);
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);

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
        return "product/product";
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

    @PostMapping("/addcart")
    @ResponseBody
    public ResponseEntity<?> addCart(@RequestBody Map<String, Object> payload, HttpSession session) {
        try {
            int productId = Integer.parseInt(payload.get("productId").toString());
            int orderQuantity = Integer.parseInt(payload.get("orderQuantity").toString());

            if (orderQuantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số lượng không hợp lệ");
            }
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User consumer = new User();
            if (principal instanceof CustomUserDetails user) {
                consumer = userService.findByUsername(user.getUsername());
            } else if (principal instanceof OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                consumer = userService.findByGmail(email);
            }
            @SuppressWarnings("unchecked")
            Map<String, String> selectedOptions = (Map<String, String>) payload.get("selectedOptions");
            ProductVatiants variant = productService.getVatiantByOptions(productId, selectedOptions);
            if (variant == null) {
                Cart cart = new Cart();
                cart.setStatus(1);
                cart.setOrderQuantity(orderQuantity);
                cart.setUser(consumer);
                cart.setProduct(productService.getProductByIdAndStatusTrue(productId));
                cart.setCreatedDate(LocalDateTime.now());
                cart.setModifiedDate(LocalDateTime.now());
                productService.addCart(cart);
                return ResponseEntity.ok(cart.getId());
            }

            Cart cart = new Cart();
            cart.setStatus(1);
            cart.setOrderQuantity(orderQuantity);
            cart.setUser(consumer);
            cart.setProduct(variant.getProduct());
            cart.setProductVatiants(variant);
            cart.setCreatedDate(LocalDateTime.now());
            cart.setModifiedDate(LocalDateTime.now());
            productService.addCart(cart);
            return ResponseEntity.ok(cart.getId());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thêm vào được giỏ hàng!");
        }
    }

    @GetMapping("/review")
    public String reviewProduct(@RequestParam("cartId") int cartId, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            consumer = userService.findByGmail(email);
        }
        UserDto userData = userMapper.toUserDto(consumer);
        Cart cart = cartRepository.findByIdAndStatusTrue(cartId);
        model.addAttribute("user", userData);
        model.addAttribute("productId", cart.getProduct().getId());
        model.addAttribute("cart", cart);
        return "home/review";
    }

    @PostMapping("/review/submit")
    public String saveReview(
            @RequestParam("productId") int productId,
            @RequestParam("rating") int rating,
            @RequestParam("comment") String comment,
            @RequestParam("images") MultipartFile[] images,
            Model model) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            consumer = userService.findByGmail(email);
        }

        Product product = productRepository.findByIdAndStatusTrue(productId);
        double oldRating = product.getRating();
        int oldRated = product.getRated();
        double newRating = rating;
        double updatedAverage = ((oldRating * oldRated) + newRating) / (oldRated + 1);
        updatedAverage = Math.round(updatedAverage * 10.0) / 10.0;
        product.setRated(oldRated + 1);
        product.setRating(updatedAverage);
        productRepository.save(product);

        ProductPreview preview = new ProductPreview();
        preview.setPreviewer(consumer);
        preview.setProduct(product);
        preview.setStar(rating);
        preview.setContent(comment);
        preview.setLiked(0);
        preview.setStatus(1);
        preview.setCreatedDate(LocalDate.now());
        preview.setModifiedDate(LocalDate.now());
        productPreviewRepository.save(preview);

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String imageUrl = cloudinaryService.getImageUrl(image);
                PreviewImage previewImage = new PreviewImage();
                previewImage.setProductPreview(preview);
                previewImage.setImageUrl(imageUrl);
                previewImage.setCreatedDate(LocalDate.now());
                previewImageRepository.save(previewImage);

            }
        }

        return "redirect:/buyer/product?productId=" + productId;
    }

}
