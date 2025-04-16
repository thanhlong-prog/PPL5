package com.code.shopee.controller.authen;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.code.shopee.Config.SmsConfig;
import com.code.shopee.dto.MailDto;
import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.enums.Role;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Roles;
import com.code.shopee.model.User;
import com.code.shopee.request.SmsRequest;
import com.code.shopee.service.MailService;
import com.code.shopee.service.RolesService;
import com.code.shopee.service.SmsService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class AuthenController {

    @Autowired 
    private UserService userService;
    @Autowired
    private RolesService rolesService;

    @Autowired
    private MailService mailService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SmsConfig smsConfig;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @RequestMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("buyer"))) {
                return "redirect:/home";
            }
        } else if (principal instanceof OAuth2User oauth2User) {
            if(oauth2User.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("buyer"))) {
                return "redirect:/home";
            };
        }
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
        return "buyer/login";
    }

    @RequestMapping("/register")
    public String registerPage(@RequestParam(value = "error", required = false) String error, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("buyer"))) {
                return "redirect:/home";
            }
        } else if (principal instanceof OAuth2User oauth2User) {
            if(oauth2User.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("buyer"))) {
                return "redirect:/home";
            };
        }
        if (error != null) {
            String message = (String) model.getAttribute("message");
            if (message != null) {
                model.addAttribute("message", message);
            }
        }
        return "buyer/register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("RegistrationDto") RegistrationDto user, BindingResult result,  RedirectAttributes redirectAttributes, HttpSession session) {
        
        if (result.hasErrors()) {
            return "redirect:/register?error=true";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập đã tồn tại!");
            return "redirect:/register?error=true";
        }
        String codeMail = session.getAttribute("codeMail") != null ? session.getAttribute("codeMail").toString() : null;
        String codePhone = session.getAttribute("codePhone") != null ? session.getAttribute("codePhone").toString() : null;
        if(user.getCodeMail() == null || !user.getCodeMail().equals(codeMail)) {
            redirectAttributes.addFlashAttribute("message", "Mã xác thực không đúng!");
            return "redirect:/register?error=true";
        }
        if(user.getCodePhone() == null || !user.getCodePhone().equals(codePhone)) {
            redirectAttributes.addFlashAttribute("message", "Mã xác thực không đúng!");
            return "redirect:/register?error=true";
        }
        Set<Roles> roles = new HashSet<>();
        roles.add(rolesService.findById(Role.BUYER.getCode()));
        user.setRoles(roles);
        user.setAvatar("https://res.cloudinary.com/dowc3tobj/image/upload/v1744467877/Image/zaqfhfcloizdf83uu8gl.jpg");
        user.setVerify(true);
        user.setEnable(true);
        user.setStatus(true);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }

    @PostMapping("/register/sendMail")
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

    @PostMapping("/register/sendPhone")
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
