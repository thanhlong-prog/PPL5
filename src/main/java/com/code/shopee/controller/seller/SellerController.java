package com.code.shopee.controller.seller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.code.shopee.dto.UserDto;
import com.code.shopee.dto.addProductDto;
import com.code.shopee.dto.variantDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.Category;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductImage;
import com.code.shopee.model.ProductOptionValues;
import com.code.shopee.model.ProductOptions;
import com.code.shopee.model.ProductVatiants;
import com.code.shopee.model.Subcategory;
import com.code.shopee.model.User;
import com.code.shopee.repository.CategoryRepository;
import com.code.shopee.repository.ProductImageRepository;
import com.code.shopee.repository.ProductOptionValuesRepo;
import com.code.shopee.repository.ProductOptionsReposioty;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.repository.ProductVatiantsRepo;
import com.code.shopee.repository.SubcategoryRepo;
import com.code.shopee.service.CloudinaryService;
import com.code.shopee.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

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

            Set<ProductOptionValues> optionValuesSet = new HashSet<>();
            List<String> attributes = variant.getAttributes();
            List<String> values = variant.getValues();

            for (int i = 0; i < attributes.size(); i++) {
                String attr = attributes.get(i);
                String val = values.get(i);

                ProductOptionValues optionValue = savedOptionValues.get(attr).get(val);
                if (optionValue != null) {
                    optionValuesSet.add(optionValue);
                }
            }
            productVariant.setOptionValues(optionValuesSet);
            productVariantsRepo.save(productVariant);
        }
        model.addAttribute("user", userData);

        return "seller/seller-add-products";
    }

    @ResponseBody
    @GetMapping("/subcategories")
    public List<Subcategory> updateSubcategory(@RequestParam("categoryId") int categoryId) {
        List<Subcategory> subcategories = subcategoryRepo.findByCategoryIdAndStatusTrue(categoryId);
        return subcategories;
    }
}
