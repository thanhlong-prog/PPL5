package com.code.shopee.controller.seller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.code.shopee.dto.UserDto;
import com.code.shopee.dto.addProductDto;
import com.code.shopee.dto.variantDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.Cart;
import com.code.shopee.model.Category;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductImage;
import com.code.shopee.model.ProductOptionValues;
import com.code.shopee.model.ProductOptions;
import com.code.shopee.model.ProductVatiants;
import com.code.shopee.model.Subcategory;
import com.code.shopee.model.Transaction;
import com.code.shopee.model.User;
import com.code.shopee.repository.CartRepository;
import com.code.shopee.repository.CategoryRepository;
import com.code.shopee.repository.ProductImageRepository;
import com.code.shopee.repository.ProductOptionValuesRepo;
import com.code.shopee.repository.ProductOptionsReposioty;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.repository.ProductVatiantsRepo;
import com.code.shopee.repository.SubcategoryRepo;
import com.code.shopee.repository.TransactionRepository;
import com.code.shopee.service.CloudinaryService;
import com.code.shopee.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubcategoryRepo subcategoryRepo;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductOptionsReposioty productOpRepo;
    @Autowired
    private ProductOptionValuesRepo productOptionValuesRepo;
    @Autowired
    private ProductVatiantsRepo productVariantsRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CartRepository cartRepo;

    @RequestMapping("/addproduct")
    public String addProduct(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);

        List<Category> categories = categoryRepository.findByStatusTrue();
        model.addAttribute("categories", categories);
        return "seller/seller-add-products";
    }

    @PostMapping("/saveproduct")
    public String saveProduct(@Valid @ModelAttribute("addProductDto") addProductDto product,
            @RequestParam("images") List<MultipartFile> images, @RequestParam("variantJson") String variantJson,
            BindingResult bindingResult,
            Model model) throws Exception, JsonProcessingException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);

        ObjectMapper objectMapper = new ObjectMapper();
        List<variantDto> variants = objectMapper.readValue(
                variantJson,
                new TypeReference<List<variantDto>>() {
                });
        // System.out.println("variants: " + variants);
        product.setVariants(variants);
        Product newProduct = new Product();
        newProduct.setTitle(product.getProductName());
        newProduct.setDescription(product.getDescription());
        newProduct.setCreatedDate(LocalDate.now());
        newProduct.setModifiedDate(LocalDate.now());
        newProduct.setSubcategory(subcategoryRepo.findByIdAndStatusTrue(product.getSubcategoryId()));
        newProduct.setLiked(0);
        newProduct.setQuantity(product.getStock());
        newProduct.setRated(0);
        newProduct.setRating(0.0);
        newProduct.setSold(0);
        newProduct.setStatus(false);
        newProduct.setSeller(consumer);
        newProduct.setBan(false);
        productRepository.save(newProduct);
        System.out.println("images: " + images.size());
        boolean isFirstImage = true;
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String imageUrl = cloudinaryService.getImageUrl(image);
                ProductImage productImage = new ProductImage();
                productImage.setProduct(newProduct);
                productImage.setImageUrl(imageUrl);
                productImage.setCreatedDate(LocalDate.now());
                productImage.setModifiedDate(LocalDate.now());
                productImage.setStatus(1);
                productImageRepository.save(productImage);
                if (isFirstImage) {
                    newProduct.setThumbnail(imageUrl);
                    productRepository.save(newProduct);
                    isFirstImage = false;
                }
            }
        }
        Map<String, Set<String>> attributeValuesMap = new HashMap<>();

        for (variantDto variant : variants) {
            List<String> attributes = variant.getAttributes();
            List<String> values = variant.getValues();

            for (int i = 0; i < attributes.size(); i++) {
                String attr = attributes.get(i);
                String val = values.get(i);

                attributeValuesMap
                        .computeIfAbsent(attr, k -> new HashSet<>())
                        .add(val);
            }
        }

        Map<String, ProductOptions> savedOptions = new HashMap<>();
        Map<String, Map<String, ProductOptionValues>> savedOptionValues = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : attributeValuesMap.entrySet()) {
            String attribute = entry.getKey();
            Set<String> values = entry.getValue();

            ProductOptions option = new ProductOptions();
            option.setName(attribute);
            option.setProduct(newProduct);
            option.setCreatedDate(LocalDate.now());
            option.setModifiedDate(LocalDate.now());
            option.setStatus(1);
            option.setCreatedBy(consumer);
            option.setDescription("no description");
            productOpRepo.save(option);
            savedOptions.put(attribute, option);

            Map<String, ProductOptionValues> valueMap = new HashMap<>();
            for (String value : values) {
                ProductOptionValues optionValue = new ProductOptionValues();
                optionValue.setValue(value);
                optionValue.setOption(option);
                optionValue.setCreatedDate(LocalDate.now());
                optionValue.setModifiedDate(LocalDate.now());
                optionValue.setStatus(1);
                optionValue.setCreatedBy(consumer);
                optionValue.setDescription("no description");
                productOptionValuesRepo.save(optionValue);

                valueMap.put(value, optionValue);
            }
            savedOptionValues.put(attribute, valueMap);
        }

        int minPrice = Integer.MAX_VALUE;
        int maxPrice = Integer.MIN_VALUE;

        for (variantDto variant : variants) {
            ProductVatiants productVariant = new ProductVatiants();
            productVariant.setProduct(newProduct);
            productVariant.setPrice(variant.getPrice().intValue());
            productVariant.setQuantity(variant.getQuantity());
            productVariant.setSku(variant.getSku());
            productVariant.setImage(variant.getImage());
            productVariant.setCreatedBy(consumer);
            productVariant.setCreatedDate(LocalDate.now());
            productVariant.setModifiedDate(LocalDate.now());
            productVariant.setStatus(1);
            productVariant.setDescription("no description");
            // productVariant.setName();
            StringBuilder nameBuilder = new StringBuilder();
            Set<ProductOptionValues> optionValuesSet = new HashSet<>();
            List<String> attributes = variant.getAttributes();
            List<String> values = variant.getValues();

            for (int i = 0; i < attributes.size(); i++) {
                String attr = attributes.get(i);
                String val = values.get(i);
                nameBuilder.append(attr).append(" ").append(val);
                if (i < attributes.size() - 1) {
                    nameBuilder.append(" - ");
                }
                ProductOptionValues optionValue = savedOptionValues.get(attr).get(val);
                if (optionValue != null) {
                    optionValuesSet.add(optionValue);
                }
            }
            productVariant.setName(nameBuilder.toString());
            productVariant.setOptionValues(optionValuesSet);
            productVariantsRepo.save(productVariant);
            int price = variant.getPrice().intValue();
            if (price < minPrice)
                minPrice = price;
            if (price > maxPrice)
                maxPrice = price;
        }
        newProduct.setPricexx(minPrice + "₫ ~ " + maxPrice + "₫");
        productRepository.save(newProduct);
        model.addAttribute("user", userData);

        return "redirect:/seller";
    }

    // @GetMapping("/product")
    // public String productManager(@RequestParam(name = "status", required = false,
    // defaultValue = "all") String status,
    // Model model) {
    // Object principal =
    // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // User consumer = new User();
    // if (principal instanceof CustomUserDetails user) {
    // consumer = userService.findByUsername(user.getUsername());
    // } else if (principal instanceof OAuth2User oauth2User) {
    // consumer = userService.findByGmail(oauth2User.getAttribute("email"));
    // }
    // UserDto userData = userMapper.toUserDto(consumer);
    // List<Product> products;
    // if (status.equalsIgnoreCase("true")) {
    // products =
    // productRepo.findActiveProductsByProductVatiantsCreatedBy(consumer);
    // } else if (status.equalsIgnoreCase("false")) {
    // products =
    // productRepo.findInactiveProductsByProductVatiantsCreatedBy(consumer);
    // } else {
    // products = productRepo.findProductsByProductVatiantsCreatedBy(consumer);
    // }
    // int productCountStatusTrue = productRepository.countByStatusTrue();
    // int productCountStatusFalse = productRepository.countByStatusFalse();
    // model.addAttribute("productCountStatusTrue", productCountStatusTrue);
    // model.addAttribute("productCountStatusFalse", productCountStatusFalse);
    // model.addAttribute("user", userData);
    // model.addAttribute("products", products);
    // model.addAttribute("statusFilter", status);
    // return "seller/product-manage";
    // }

    @GetMapping("")
    public String sellerHome(@RequestParam(name = "status", required = false, defaultValue = "all") String status,
            @RequestParam(name = "page", required = false, defaultValue = "product") String page,
            Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        if (page.equalsIgnoreCase("product")) {
            List<Product> products;
            if (status.equalsIgnoreCase("true")) {
                products = productRepo.findActiveProductsAndSellerIdAndByProductVatiantsCreatedBy(consumer.getId(),
                        consumer);
            } else if (status.equalsIgnoreCase("false")) {
                products = productRepo.findInactiveAndNotBannedProductsBySellerAndCreatedBy(consumer.getId(),
                        consumer);
            } else if (status.equalsIgnoreCase("ban")) {
                products = productRepo.findInactiveProductsAndSellerIdByProductVatiantsCreatedByAndBanned(
                        consumer.getId(),
                        consumer);
            } else {
                products = productRepo.findProductsAndSellerIdByProductVatiantsCreatedBy(consumer.getId(), consumer);
            }
            int productCountStatusTrue = productRepository.countBySellerIdAndStatusTrue(consumer.getId());
            int productCountStatusFalse = productRepository
                    .countBySellerIdAndStatusFalseAndIsBanFalse(consumer.getId());
            int productCountStatusBaned = productRepository.countBySellerIdAndStatusFalseAndIsBanTrue(consumer.getId());
            int productCountAll = productRepository.countBySellerId(consumer.getId());
            model.addAttribute("productCountStatusTrue", productCountStatusTrue);
            model.addAttribute("productCountStatusFalse", productCountStatusFalse);
            model.addAttribute("productCountStatusBaned", productCountStatusBaned);
            model.addAttribute("productCountAll", productCountAll);
            model.addAttribute("user", userData);
            model.addAttribute("products", products);
            model.addAttribute("statusFilter", status);
            model.addAttribute("mode", page);
        } else if (page.equalsIgnoreCase("purchase")) {
            List<Transaction> transactions;
            if (status.equalsIgnoreCase("all")) {
                transactions = transactionRepository.findAllBySellerId(consumer.getId());
            } else {
                int shippingStatusFilter = -1;
                try {
                    shippingStatusFilter = Integer.parseInt(status);
                } catch (NumberFormatException e) {
                    shippingStatusFilter = -1;
                }
                transactions = transactionRepository.findAllBySellerIdAndShippingStatus(consumer.getId(),
                        shippingStatusFilter);
            }
            for (Transaction t : transactions) {
                int total = t.getCarts().stream().mapToInt(Cart::getTotalPrice).sum();
                t.setTotal(total);
                long distinctStatuses = t.getCarts().stream()
                        .map(Cart::getShippingStatus)
                        .distinct()
                        .count();

                if (distinctStatuses == 1) {
                    int commonStatus = t.getCarts().get(0).getShippingStatus();
                    t.setShippingStatus(commonStatus);
                } else {
                    t.setShippingStatus(-1);
                }
            }
            int confirmCart = cartRepo.countBySellerIdAndShippingStatus(consumer.getId(), 2);
            int packCart = cartRepo.countBySellerIdAndShippingStatus(consumer.getId(), 3);
            int waitCart = cartRepo.countBySellerIdAndShippingStatus(consumer.getId(), 4);
            int deliveredCart = cartRepo.countBySellerIdAndShippingStatus(consumer.getId(), 5);
            int canceledCart = cartRepo.countBySellerIdAndShippingStatus(consumer.getId(), 6);
            int onDeliveryCart = cartRepo.countBySellerIdAndShippingStatus(consumer.getId(), 1);
            int allCart = cartRepo.countBySellerId(consumer.getId());
            model.addAttribute("confirmCart", confirmCart);
            model.addAttribute("packCart", packCart);
            model.addAttribute("waitCart", waitCart);
            model.addAttribute("deliveredCart", deliveredCart);
            model.addAttribute("canceledCart", canceledCart);
            model.addAttribute("onDeliveryCart", onDeliveryCart);
            model.addAttribute("transactions", transactions);
            model.addAttribute("allCart", allCart);
            model.addAttribute("mode", page);
        } else if (page.equalsIgnoreCase("cancel")) {
            List<Transaction> transactions = transactionRepository.findAllBySellerIdAndShippingStatus(consumer.getId(),
                    6);
            for (Transaction t : transactions) {
                int total = t.getCarts().stream().mapToInt(Cart::getTotalPrice).sum();
                t.setTotal(total);
                long distinctStatuses = t.getCarts().stream()
                        .map(Cart::getShippingStatus)
                        .distinct()
                        .count();

                if (distinctStatuses == 1) {
                    int commonStatus = t.getCarts().get(0).getShippingStatus();
                    t.setShippingStatus(commonStatus);
                } else {
                    t.setShippingStatus(-1);
                }
            }
            int canceledCart = cartRepo.countBySellerIdAndShippingStatus(consumer.getId(), 6);
            model.addAttribute("canceledCart", canceledCart);
            model.addAttribute("transactions", transactions);
            model.addAttribute("mode", page);
        } else if (page.equalsIgnoreCase("bulk")) {
            List<Transaction> transactions = transactionRepository.findAllBySellerIdAndShippingStatus(consumer.getId(),
                    3);
            for (Transaction t : transactions) {
                int total = t.getCarts().stream().mapToInt(Cart::getTotalPrice).sum();
                t.setTotal(total);
                long distinctStatuses = t.getCarts().stream()
                        .map(Cart::getShippingStatus)
                        .distinct()
                        .count();

                if (distinctStatuses == 1) {
                    int commonStatus = t.getCarts().get(0).getShippingStatus();
                    t.setShippingStatus(commonStatus);
                } else {
                    t.setShippingStatus(-1);
                }
            }
            model.addAttribute("transactions", transactions);
            model.addAttribute("mode", page);
        } else if (page.equalsIgnoreCase("analysis")) {
            model.addAttribute("mode", page);
        }

        model.addAttribute("user", userData);
        return "seller/seller-manage";
    }

    @ResponseBody
    @GetMapping("/subcategories")
    public List<Subcategory> updateSubcategory(@RequestParam("categoryId") int categoryId) {
        List<Subcategory> subcategories = subcategoryRepo.findByCategoryIdAndStatusTrue(categoryId);
        return subcategories;
    }

    @PostMapping("/confirm-delivery")
    @ResponseBody
    public ResponseEntity<?> confirmDelivery(@RequestBody Map<String, List<Integer>> body) {
        List<Integer> transactionIds = body.get("transactionIds");
        for (int transactionId : transactionIds) {
            Transaction transaction = transactionRepository.findByIdAndStatusTrue(transactionId);
            if (transaction == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Transaction not found or inactive"));
            }
            List<Cart> carts = cartRepo.findAllByTransactionId(transactionId);
            for (Cart cart : carts) {
                cart.setShippingStatus(1);
                cart.setModifiedDate(LocalDateTime.now());
                cartRepo.save(cart);
            }
            transaction.setShippingStatus(1);
            transactionRepository.save(transaction);
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/accept-purchase")
    public ResponseEntity<?> acceptPurchase(@RequestParam int transactionId) {
        Transaction transaction = transactionRepository.findByIdAndStatusTrue(transactionId);
        if (transaction == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Transaction not found or inactive"));
        }
        List<Cart> carts = cartRepo.findAllByTransactionId(transactionId);
        for (Cart cart : carts) {
            cart.setShippingStatus(3);
            cart.setModifiedDate(LocalDateTime.now());
            cartRepo.save(cart);
        }
        transaction.setShippingStatus(3);
        transaction.setModifiedDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/get-revenue")
    @ResponseBody
    public ResponseEntity<?> getRevenue(@RequestBody Map<String, String> body) {
        LocalDate startDate = LocalDate.parse(body.get("startDate"));
        LocalDate endDate = LocalDate.parse(body.get("endDate"));
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }

        List<Cart> carts = cartRepo.findBySellerIdAndTransactionCreatedDateBetween(
                consumer.getId(), startDateTime, endDateTime);

        Map<LocalDate, Integer> revenueByDate = new LinkedHashMap<>();
        Map<LocalDate, Integer> cartCountByDate = new LinkedHashMap<>();
        Map<LocalDate, Integer> successByDate = new LinkedHashMap<>();
        Map<LocalDate, Integer> cancelCountByDate = new LinkedHashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            revenueByDate.put(date, 0);
            cartCountByDate.put(date, 0);
            successByDate.put(date, 0);
            cancelCountByDate.put(date, 0);
        }

        for (Cart cart : carts) {
            if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                revenueByDate.put(createdDate, revenueByDate.get(createdDate) + cart.getTotalPrice());
                cartCountByDate.put(createdDate, cartCountByDate.get(createdDate) + 1);
            }

            if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1
                    && cart.getShippingStatus() == 5) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                successByDate.put(createdDate, successByDate.get(createdDate) + 1);
            } else if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1
                    && cart.getShippingStatus() == 6) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                cancelCountByDate.put(createdDate, cancelCountByDate.get(createdDate) + 1);
            }
        }

        return ResponseEntity.ok(Map.of(
                "dailyRevenue", new ArrayList<>(revenueByDate.values()),
                "dailyCartCount", new ArrayList<>(cartCountByDate.values()),
                "dailySuccessCount", new ArrayList<>(successByDate.values()),
                "dailyCancelCount", new ArrayList<>(cancelCountByDate.values())));
    }

    @PostMapping("/get-revenue-month")
    @ResponseBody
    public ResponseEntity<?> getRevenueByMonth(@RequestBody Map<String, String> body) {
        int year = Integer.parseInt(body.get("year"));
        int month = Integer.parseInt(body.get("month"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }

        List<Cart> carts = cartRepo.findBySellerIdAndTransactionCreatedDateBetween(
                consumer.getId(), startDateTime, endDateTime);

        Map<Integer, Integer> revenueByDay = new LinkedHashMap<>();
        Map<Integer, Integer> cartCountByDay = new LinkedHashMap<>();
        Map<Integer, Integer> successByDay = new LinkedHashMap<>();
        Map<Integer, Integer> cancelCountByDay = new LinkedHashMap<>();
        for (int day = 1; day <= startDate.lengthOfMonth(); day++) {
            revenueByDay.put(day, 0);
            cartCountByDay.put(day, 0);
            successByDay.put(day, 0);
            cancelCountByDay.put(day, 0);
        }

        for (Cart cart : carts) {
            if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                int day = createdDate.getDayOfMonth();
                revenueByDay.put(day, revenueByDay.get(day) + cart.getTotalPrice());
                cartCountByDay.put(day, cartCountByDay.get(day) + 1);
            }

            if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1
                    && cart.getShippingStatus() == 5) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                int day = createdDate.getDayOfMonth();
                successByDay.put(day, successByDay.get(day) + 1);
            } else if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1
                    && cart.getShippingStatus() == 6) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                int day = createdDate.getDayOfMonth();
                cancelCountByDay.put(day, cancelCountByDay.get(day) + 1);
            }
        }

        return ResponseEntity.ok(Map.of(
                "dailyRevenue", new ArrayList<>(revenueByDay.values()),
                "dailyCartCount", new ArrayList<>(cartCountByDay.values()),
                "dailySuccessCount", new ArrayList<>(successByDay.values()),
                "dailyCancelCount", new ArrayList<>(cancelCountByDay.values())));
    }

    @PostMapping("/get-revenue-quarter")
    @ResponseBody
    public ResponseEntity<?> getRevenueByQuarter(@RequestBody Map<String, String> body) {
        int year = Integer.parseInt(body.get("year"));
        int quarter = Integer.parseInt(body.get("quarter")); // 1, 2, 3, hoặc 4

        int startMonth = (quarter - 1) * 3 + 1;
        LocalDate startDate = LocalDate.of(year, startMonth, 1);
        LocalDate endDate = startDate.plusMonths(3).minusDays(1);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }

        List<Cart> carts = cartRepo.findBySellerIdAndTransactionCreatedDateBetween(
                consumer.getId(), startDateTime, endDateTime);

        Map<Integer, Integer> revenueByMonth = new LinkedHashMap<>();
        Map<Integer, Integer> cartCountByMonth = new LinkedHashMap<>();
        Map<Integer, Integer> successByMonth = new LinkedHashMap<>();
        Map<Integer, Integer> cancelCountByMonth = new LinkedHashMap<>();

        for (int m = startMonth; m < startMonth + 3; m++) {
            revenueByMonth.put(m, 0);
            cartCountByMonth.put(m, 0);
            successByMonth.put(m, 0);
            cancelCountByMonth.put(m, 0);
        }

        for (Cart cart : carts) {
            if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                int month = createdDate.getMonthValue();
                revenueByMonth.put(month, revenueByMonth.get(month) + cart.getTotalPrice());
                cartCountByMonth.put(month, cartCountByMonth.get(month) + 1);

            }

            if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1
                    && cart.getShippingStatus() == 5) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                int month = createdDate.getMonthValue();
                successByMonth.put(month, successByMonth.get(month) + 1);
            } else if (cart.getTransaction() != null && cart.getTransaction().getStatus() == 1
                    && cart.getShippingStatus() == 6) {
                LocalDate createdDate = cart.getTransaction().getCreatedDate().toLocalDate();
                int month = createdDate.getMonthValue();
                cancelCountByMonth.put(month, cancelCountByMonth.get(month) + 1);
            }
        }

        return ResponseEntity.ok(Map.of(
                "monthlyRevenue", new ArrayList<>(revenueByMonth.values()),
                "monthlyCartCount", new ArrayList<>(cartCountByMonth.values()),
                "monthlySuccessCount", new ArrayList<>(successByMonth.values()),
                "monthlyCancelCount", new ArrayList<>(cancelCountByMonth.values())));
    }

}
