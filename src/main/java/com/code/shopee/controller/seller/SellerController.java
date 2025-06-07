package com.code.shopee.controller.seller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
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
        System.out.println("variants: " + variants);
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

        for (variantDto variant : variants) {
            ProductVatiants productVariant = new ProductVatiants();
            productVariant.setProduct(newProduct);
            productVariant.setPrice(variant.getPrice().intValue());
            productVariant.setQuantity(variant.getQuantity());
            productVariant.setSku(variant.getSku());
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
        }
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
                products = productRepo.findInactiveProductsAndSellerIdByProductVatiantsCreatedBy(consumer.getId(),
                        consumer);
            } else {
                products = productRepo.findProductsAndSellerIdByProductVatiantsCreatedBy(consumer.getId(), consumer);
            }
            int productCountStatusTrue = productRepository.countBySellerIdAndStatusTrue(consumer.getId());
            int productCountStatusFalse = productRepository.countBySellerIdAndStatusFalse(consumer.getId());
            model.addAttribute("productCountStatusTrue", productCountStatusTrue);
            model.addAttribute("productCountStatusFalse", productCountStatusFalse);
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
                cart.setShippingStatus(4);
                cart.setModifiedDate(LocalDateTime.now());
                cartRepo.save(cart);
            }
            transaction.setShippingStatus(4);
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

}
