package com.code.shopee.controller.buyer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.code.shopee.Config.SmsConfig;
import com.code.shopee.dto.MailDto;
import com.code.shopee.dto.SellerBecomeDto;
import com.code.shopee.dto.UserDto;
import com.code.shopee.dto.VerifyUserDto;
import com.code.shopee.mapper.SellerBecomeMapper;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.SellerInfo;
import com.code.shopee.model.User;
import com.code.shopee.repository.SellerInfoRepo;
import com.code.shopee.request.SmsRequest;
import com.code.shopee.service.CloudinaryService;
import com.code.shopee.service.MailService;
import com.code.shopee.service.SmsService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/buyer")
public class BuyerController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SmsConfig smsConfig;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SellerBecomeMapper sellerBecomeMapper;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private SellerInfoRepo sellerInfoRepo;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @RequestMapping("/seller-register")
    public String sellerRegister(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);

        return "seller/seller-register";
    }

    @PostMapping("/seller-request")
    public String sellerRequest(@Valid @ModelAttribute("SellerBecomeDto") SellerBecomeDto sellerInfo,
            BindingResult bindingResult, @RequestParam("front-img") MultipartFile frontImg,
            @RequestParam("back-img") MultipartFile backImg, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User consumer = new User();
            if (principal instanceof CustomUserDetails user) {
                consumer = userService.findByUsername(user.getUsername());
            } else if (principal instanceof OAuth2User oauth2User) {
                consumer = userService.findByGmail(oauth2User.getAttribute("email"));
            }
            UserDto userData = userMapper.toUserDto(consumer);
            model.addAttribute("user", userData);

            return "seller/seller-register";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        if (frontImg != null && !frontImg.isEmpty() && backImg != null && !backImg.isEmpty()) {
            try {
                String frontImgUrl = cloudinaryService.getImageUrl(frontImg);
                String backImgUrl = cloudinaryService.getImageUrl(backImg);
                sellerInfo.setIdentificationFront(frontImgUrl);
                sellerInfo.setIdentificationBack(backImgUrl);
                SellerInfo seller = sellerBecomeMapper.toSeller(sellerInfo);
                seller.setUser(consumer);
                if (seller.getId() == null) {
                    seller.setCreatedDate(LocalDateTime.now());
                    seller.setVerify(false);
                    seller.setStatus(1);
                } else {
                    seller.setModifiedDate(LocalDateTime.now());
                }
                sellerInfoRepo.save(seller);
            } catch (Exception e) {
                model.addAttribute("errors", "Vui lòng upload ảnh CCCD hợp lệ");
                return "seller/seller-register";
            }
        }
        return "redirect:/home";
    }

    @RequestMapping("/verify")
    public String verify(@RequestParam(value = "error", required = false) String error, Model model,
            HttpSession session) {
        User user = new User();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            user = userService.findByUsername(userDetails.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            user = userService.findByGmail(email);
        }
        if (user.getVerify()) {
            return "redirect:/home";
        }
        VerifyUserDto verifyUser = new VerifyUserDto();
        if (error != null) {
            Map<String, String> errors = (Map<String, String>) model.getAttribute("message");
            if (errors != null) {
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    model.addAttribute(entry.getKey(), entry.getValue());
                }
                verifyUser = (VerifyUserDto) model.getAttribute("verifyUser");
            }
        }
        Boolean isFirst = (Boolean) session.getAttribute("is_first");
        if (isFirst == null || isFirst) {
            verifyUser.setGmail(user.getGmail());
            verifyUser.setName(user.getName());
            session.setAttribute("is_first", false);
        }
        if (verifyUser != null) {
            model.addAttribute("verifyUser", verifyUser);
        }
        return "buyer/info-form";
    }

    @PostMapping("/verify/submit")
    public String submitVerify(@Valid @ModelAttribute("VerifyUserDto") VerifyUserDto verifyUser, BindingResult result,
            RedirectAttributes redirectAttributes, HttpSession session) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError field : result.getFieldErrors()) {
                String fieldName = field.getField();
                String errorMessage = field.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            }
            redirectAttributes.addFlashAttribute("message", errors);
            redirectAttributes.addFlashAttribute("verifyUser", verifyUser);
            return "redirect:/buyer/verify?error=true";
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            verifyUser.setUsername(userDetails.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            verifyUser.setUsername(userService.findByGmail(email).getUsername());
        }
        String codeMail = session.getAttribute("codeMail") != null ? session.getAttribute("codeMail").toString() : null;
        String codePhone = session.getAttribute("codePhone") != null ? session.getAttribute("codePhone").toString()
                : null;
        if (verifyUser.getCodeMail().equals(codeMail) && verifyUser.getCodePhone().equals(codePhone)) {
            verifyUser.setModifiedDate(LocalDateTime.now());
            verifyUser.setVerify(true);
            userService.save(verifyUser);
        } else {
            Map<String, String> errors = new HashMap<>();
            if (!verifyUser.getCodeMail().equals(codeMail)) {
                errors.put("codeMail", "Mã xác thực gmail sai!");
            }
            if (!verifyUser.getCodePhone().equals(codePhone)) {
                errors.put("codePhone", "Mã xác thực số điện thoại sai!");
            }
            redirectAttributes.addFlashAttribute("message", errors);
            redirectAttributes.addFlashAttribute("verifyUser", verifyUser);
            return "redirect:/buyer/verify?error=true";
        }
        return "redirect:/home";
    }

    @PostMapping("/verify/sendMail")
    @ResponseBody
    public ResponseEntity<?> sendMail(@RequestParam("email") String email, HttpSession session) {
        try {
            String userGmail = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails userDetails) {
                userGmail = userDetails.getGmail();
            } else if (principal instanceof OAuth2User oauth2User) {
                userGmail = oauth2User.getAttribute("email");
            }
            if (checkMailExist(email, userGmail)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Email đã tồn tại trong hệ thống");
            }
            String codeMail = randomCode();
            session.setAttribute("codeMail", codeMail);
            scheduler.schedule(() -> {
                session.removeAttribute("codeMail");
            }, 300, TimeUnit.SECONDS);
            MailDto mess = new MailDto(email, "Mã xác thực gmail", codeMail);
            mailService.sendEmail(mess);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email thất bại");
        }
    }

    @PostMapping("/verify/sendPhone")
    @ResponseBody
    public ResponseEntity<?> sendPhone(@RequestParam("phone") String phone, HttpSession session) {
        try {
            checkPhoneExist(phone);
            if (checkPhoneExist(phone)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Số điện thoại đã tồn tại trong hệ thống");
            }
            String codePhone = randomCode();
            session.setAttribute("codePhone", codePhone);
            scheduler.schedule(() -> {
                session.removeAttribute("codePhone");
            }, 300, TimeUnit.SECONDS);
            SmsRequest smsRequest = new SmsRequest(phone, codePhone, 2, smsConfig.getDeviceId());
            Boolean check = smsService.sendSms(smsRequest);
            if (!check) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Số điện thoại không hợp lệ");
            }
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi tin nhắn thất bại");
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

    public Boolean checkMailExist(String email, String userGmail) {
        User user = userService.findByGmail(email);
        if (user == null || email.equals(userGmail)) {
            return false;
        }
        return true;
    }

    public Boolean checkPhoneExist(String phone) {
        User user = userService.findByPhone(phone);
        return user == null ? false : true;
    }
}
