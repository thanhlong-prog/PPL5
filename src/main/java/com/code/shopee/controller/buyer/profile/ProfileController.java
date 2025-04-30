package com.code.shopee.controller.buyer.profile;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.code.shopee.Config.SmsConfig;
import com.code.shopee.dto.MailDto;
import com.code.shopee.dto.ProfileDto;
import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.User;
import com.code.shopee.request.SmsRequest;
import com.code.shopee.service.CloudinaryService;
import com.code.shopee.service.MailService;
import com.code.shopee.service.SmsService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/buyer/profile")
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private MailService mailService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SmsConfig smsConfig;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @RequestMapping("")
    public String profile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if(principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user",userData);
        return "profile/profile";
    }

    @PostMapping("/save")
    public String saveProfile(@ModelAttribute("ProfileDto") ProfileDto user, @RequestParam("avatar-img") MultipartFile avatar, @RequestParam("current-avatar") String currentAvatar, RedirectAttributes redirectAttributes) {
        if(avatar != null && !avatar.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.getImageUrl(avatar);
                user.setAvatar(imageUrl);
                user.setAvatar(imageUrl);
                userService.save(user);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("upload-error", "Error uploading file");
                return "redirect:/buyer/profile";
            }
        }
        else {
            user.setAvatar(currentAvatar);
            userService.save(user);
        }
        return "redirect:/buyer/profile";
    }

    @PostMapping("/sendMail")
    @ResponseBody
    public ResponseEntity<?> sendMail(@RequestParam("email") String email, HttpSession session) {
        try {
            String userGmail = null;
            if(checkMailExist(email)) {
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

    @PostMapping("/sendPhone")
    @ResponseBody
    public ResponseEntity<?> sendPhone(@RequestParam("phone") String phone, HttpSession session) {
        try {
            checkPhoneExist(phone);
            if(checkPhoneExist(phone)) {
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
            if(!check) {
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

    public Boolean checkMailExist(String email) {
        User user = userService.findByGmail(email);
        if(user == null) {
            return false;
        } 
        return true;
    }
    public Boolean checkPhoneExist(String phone) {
        User user = userService.findByPhone(phone);
        return user == null ? false : true;
    }

}

