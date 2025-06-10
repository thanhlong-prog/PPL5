package com.code.shopee.controller.buyer.profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.code.shopee.model.Cart;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Notification;
import com.code.shopee.model.Reason;
import com.code.shopee.model.Transaction;
import com.code.shopee.model.User;
import com.code.shopee.model.UserAddress;
import com.code.shopee.repository.CartRepository;
import com.code.shopee.repository.NotificationRepo;
import com.code.shopee.repository.ReasonRepo;
import com.code.shopee.repository.UserRepository;
import com.code.shopee.request.SmsRequest;
import com.code.shopee.service.CloudinaryService;
import com.code.shopee.service.MailService;
import com.code.shopee.service.ProductService;
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
    @Autowired
    private ProductService productService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ReasonRepo reasonRepo;
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private UserRepository UserRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @RequestMapping("")
    public String profile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        return "profile/profile";
    }

    @RequestMapping("/password")
    public String password(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        return "profile/password";
    }

    @PostMapping("/password/save")
    public String savePassword(@RequestParam("current-password") String currentPassword,
            @RequestParam("new-password") String newPassword, @RequestParam("retype-password") String retypePassword,
            Model model, RedirectAttributes redirectAttributes) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserDto userData = userMapper.toUserDto(consumer);
        if (!encoder.matches(currentPassword, consumer.getPassword())) {
            model.addAttribute("user", userData);
            model.addAttribute("error", "Mật khẩu hiện tại không đúng");
            return "profile/password";
        }
        if (!newPassword.equals(retypePassword)) {
            model.addAttribute("user", userData);
            model.addAttribute("error", "Mật khẩu mới không khớp");
            return "profile/password";
        }
        consumer.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        UserRepository.save(consumer);
        redirectAttributes.addFlashAttribute("success", "Cập nhật mật khẩu thành công");
        return "redirect:/buyer/profile/password";
    }

    @RequestMapping("/shopee")
    public String shopee() {
        return "profile/shopee";
    }

    @RequestMapping("/purchase")
    public String purchase(@RequestParam(name = "mode", required = false, defaultValue = "pending") String mode,
            Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        model.addAttribute("mode", mode);
        List<Cart> cartList = switch (mode) {
            case "all" -> cartRepository.findByUserIdAndStatusTrueAndTransactionIsNotNull(consumer.getId());
            case "pending" -> productService.getAllCartWaitingForShip(consumer.getId(), 2);
            case "waiting" -> productService.getAllCartWaitingForShip(consumer.getId(), 4);
            case "shipping" -> productService.getAllCartWaitingForShip(consumer.getId(), 1);
            case "completed" -> productService.getAllCartWaitingForShip(consumer.getId(), 5);
            case "cancelled" -> productService.getAllCartWaitingForShip(consumer.getId(), 6);
            default -> productService.getAllCartWaitingForShip(consumer.getId(), 2);
        };
        Map<User, List<Cart>> groupedCartList = cartList.stream()
                .collect(Collectors.groupingBy(cart -> cart.getProduct().getSeller()));
        model.addAttribute("groupedCartList", groupedCartList);
        return "profile/purchase";
    }

    @PostMapping("/purchase/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelOrder(@RequestParam("sellerId") int sellerId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        try {
            List<Cart> carts = cartRepository.findBySellerIdAndStatusTrueAndShippingStatus2(sellerId);
            for (Cart cart : carts) {
                if (cart != null) {
                    cart.setShippingStatus(6);
                    cart.getProductVatiants()
                            .setQuantity(cart.getProductVatiants().getQuantity() + cart.getOrderQuantity());
                    cart.setModifiedDate(LocalDateTime.now());
                    cartRepository.save(cart);

                    Transaction transaction = cart.getTransaction();
                    boolean exists = reasonRepo.existsByTransaction(transaction);

                    if (!exists) {
                        Reason reason = new Reason();
                        reason.setReason("Order cancelled by user");
                        reason.setStatus("Cancelled");
                        reason.setSeller(userService.findById(sellerId));
                        reason.setTransaction(transaction);
                        reasonRepo.save(reason);
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
                }
            }
            return ResponseEntity.ok("Order cancelled successfully");
        } catch (Exception e) {
            logger.error("Error cancelling order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error cancelling order");
        }
    }

    @PostMapping("/purchase/receive")
    @ResponseBody
    public ResponseEntity<?> receiveOrder(@RequestParam("sellerId") int sellerId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        try {
            List<Cart> carts = cartRepository.findBySellerIdAndStatusTrueAndShippingStatus1(sellerId);
            User seller = UserRepository.findByIdAndStatusTrue(sellerId);
            for (Cart cart : carts) {
                if (cart != null) {
                    cart.setShippingStatus(5);
                    cart.setModifiedDate(LocalDateTime.now());
                    cartRepository.save(cart);
                    Notification notification = new Notification();
                    notification.setUser(consumer);
                    notification.setTitle("Đánh giá sản phẩm " + cart.getId() + " đã được nhận");
                    notification.setContent("Đánh giá sản phẩm của bạn đã được nhận thành công từ người bán "
                            + seller.getSellerInfo().getShopName());
                    notification.setStatus(true);
                    notification.setUrl("/buyer/product/review?cartId=" + cart.getId());
                    notification.setCreatedDate(LocalDateTime.now());
                    notificationRepo.save(notification);

                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
                }
            }
            Notification notification = new Notification();
            notification.setUser(consumer);
            notification.setTitle("Đơn hàng " + carts.get(0).getTransaction().getId() + " đã được nhận");
            notification.setContent("Đơn hàng của bạn đã được nhận thành công từ người bán "
                    + seller.getSellerInfo().getShopName());
            notification.setStatus(true);
            notification.setUrl("/buyer/profile/purchase?mode=completed");
            notification.setCreatedDate(LocalDateTime.now());
            notificationRepo.save(notification);
            return ResponseEntity.ok("Order received successfully");
        } catch (Exception e) {
            logger.error("Error receiving order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error receiving order");
        }
    }

    @RequestMapping("/address")
    public String address(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        List<UserAddress> addressList = userService.getAllUserAddressByStatusTrue(consumer.getId());
        model.addAttribute("addressList", addressList);
        return "profile/address";
    }

    @PostMapping("/address/add")
    public String addAddress(@RequestParam(value = "user-name") String fullname,
            @RequestParam(value = "user-phone") String phone,
            @RequestParam(value = "user-address") String fulladdress,
            @RequestParam(value = "user-address-detail") String location, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        UserAddress userAddress = new UserAddress();
        userAddress.setFullname(fullname);
        userAddress.setPhone(phone);
        userAddress.setFullAddress(fulladdress);
        userAddress.setLocation(location);
        userAddress.setUser(consumer);
        userAddress.setStatus(1);
        userAddress.setIsDefault(0);
        userAddress.setCreatedDate(LocalDate.now());
        userAddress.setModifiedDate(LocalDate.now());
        userService.saveUserAddress(userAddress);
        return "redirect:/buyer/profile/address";
    }

    @PostMapping("/address/saveAddress")
    public String saveAddress(@RequestParam(value = "address-id") int id,
            @RequestParam(value = "user-name") String fullname,
            @RequestParam(value = "user-phone") String phone,
            @RequestParam(value = "user-address") String fulladdress,
            @RequestParam(value = "user-address-detail") String location, Model model) {
        UserAddress userAddress = userService.getUserAddressByStatusTrue(id);
        userAddress.setFullname(fullname);
        userAddress.setPhone(phone);
        userAddress.setFullAddress(fulladdress);
        userAddress.setLocation(location);
        userAddress.setModifiedDate(LocalDate.now());
        userService.saveUserAddress(userAddress);
        return "redirect:/buyer/profile/address";
    }

    @GetMapping("/address/delAddress")
    public String delAddress(@RequestParam(value = "address-id") int id, Model model) {
        try {
            userService.deleteUserAddress(id);
        } catch (Exception e) {
            logger.error("Error deleting address: " + e.getMessage());
        }
        return "redirect:/buyer/profile/address";
    }

    @GetMapping("/address/setDefault")
    public String setDefault(@RequestParam(value = "address-id") int id, Model model) {
        UserAddress oldDefaultAddress = userService.getUserAddressByIsDefault(1);
        if (oldDefaultAddress != null) {
            oldDefaultAddress.setIsDefault(0);
            oldDefaultAddress.setModifiedDate(LocalDate.now());
            userService.saveUserAddress(oldDefaultAddress);
        }
        UserAddress userAddress = userService.getUserAddressByStatusTrue(id);
        userAddress.setIsDefault(1);
        userAddress.setModifiedDate(LocalDate.now());
        userService.saveUserAddress(userAddress);
        return "redirect:/buyer/profile/address";
    }

    @RequestMapping("/order")
    public String order(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        List<Notification> notifications = notificationRepo
                .findByUserIdAndStatusTrueOrderByCreatedDateDesc(consumer.getId());
        model.addAttribute("notifications", notifications);
        return "profile/order";
    }

    @RequestMapping("/promotion")
    public String promotion() {
        return "profile/promotion";
    }

    @RequestMapping("/voucher")
    public String voucher() {
        return "profile/voucher-wallet";
    }

    @PostMapping("/save")
    public String saveProfile(@ModelAttribute("ProfileDto") ProfileDto user,
            @RequestParam("avatar-img") MultipartFile avatar, @RequestParam("current-avatar") String currentAvatar,
            RedirectAttributes redirectAttributes) {
        if (avatar != null && !avatar.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.getImageUrl(avatar);
                user.setAvatar(imageUrl);
                user.setAvatar(imageUrl);
                userService.save(user);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("upload-error", "Error uploading file");
                return "redirect:/buyer/profile";
            }
        } else {
            user.setAvatar(currentAvatar);
            userService.save(user);
        }
        return "redirect:/buyer/profile";
    }

    @PostMapping("/sendMail")
    @ResponseBody
    public ResponseEntity<?> sendMail(@RequestParam("email") String email, HttpSession session) {
        try {
            if (checkMailExist(email)) {
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

    @PostMapping("/checkCodeMail")
    @ResponseBody
    public ResponseEntity<?> checkCodeMail(@RequestParam("codeMail") String code, HttpSession session) {
        String codeMail = session.getAttribute("codeMail") != null ? session.getAttribute("codeMail").toString() : null;
        if (codeMail != null && !codeMail.equals(code)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Xác nhận mã xác thực không thành công");
        } else if (codeMail != null && codeMail.equals(code)) {
            session.removeAttribute("codeMail");
            return ResponseEntity.ok("success");
        } else if (codeMail == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Mã xác thực đã hết hạn hoặc không tồn tại");
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/checkCodePhone")
    @ResponseBody
    public ResponseEntity<?> checkCodePhone(@RequestParam("codePhone") String code, HttpSession session) {
        String codePhone = session.getAttribute("codePhone") != null ? session.getAttribute("codePhone").toString()
                : null;
        if (codePhone != null && !codePhone.equals(code)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Xác nhận mã xác thực không thành công");
        } else if (codePhone != null && codePhone.equals(code)) {
            session.removeAttribute("codeMail");
            return ResponseEntity.ok("success");
        } else if (codePhone == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Mã xác thực đã hết hạn hoặc không tồn tại");
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/sendPhone")
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

    public Boolean checkMailExist(String email) {
        User user = userService.findByGmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }

    public Boolean checkPhoneExist(String phone) {
        User user = userService.findByPhone(phone);
        return user == null ? false : true;
    }

}
