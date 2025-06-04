package com.code.shopee.controller.buyer.order;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.code.shopee.Config.VnpayConfig;
import com.code.shopee.dto.PaymentDto;
import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.Cart;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.Transaction;
import com.code.shopee.model.User;
import com.code.shopee.model.UserAddress;
import com.code.shopee.repository.CartRepository;
import com.code.shopee.repository.TransactionRepository;
import com.code.shopee.service.ProductService;
import com.code.shopee.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/buyer/payment")
public class PaymentController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CartRepository cartRepository;

    @RequestMapping("")
    public String payment(@RequestParam(value = "cartIds") List<Integer> items, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            consumer = userService.findByGmail(email);
        }
        UserDto userData = userMapper.toUserDto(consumer);
        model.addAttribute("user", userData);
        Map<User, List<Cart>> groupedCartList = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            Cart cart = productService.getCartByIdAndStatusTrue(items.get(i));
            if (cart != null) {
                User shop = cart.getProduct().getSeller();
                groupedCartList.computeIfAbsent(shop, k -> new ArrayList<>()).add(cart);
            }
        }
        List<UserAddress> userAddressList = userService.getAllUserAddressByStatusTrue(consumer.getId());
        for (UserAddress userAddress : userAddressList) {
            if (userAddress.getIsDefault() == 1) {
                model.addAttribute("userAddress", userAddress);
                break;
            }
        }
        model.addAttribute("userAddressList", userAddressList);
        model.addAttribute("groupedCartList", groupedCartList);
        return "checkout/checkout";
    }

    @GetMapping("/addpayment")
    public String addPayment(@RequestParam(value = "totalPay") int amount,
            @RequestParam(value = "cartIds") List<Integer> items,
            @RequestParam(value = "addressId") int addressId, Model model) {

        return "redirect:/buyer/payment/create?amount=" + amount;
    }

    @GetMapping("/create")
    public String createPayment(HttpServletRequest req, Model model, HttpServletRequest request,
            @RequestParam(value = "totalPay") int amountres,
            @RequestParam(value = "cartIds") List<Integer> items,
            @RequestParam(value = "addressId") int addressId)
            throws UnsupportedEncodingException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User consumer = new User();
        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            consumer = userService.findByGmail(email);
        }
        String orderType = "other";
        long amount = amountres * 100;
        // long amount = 10000000;
        String bankCode = req.getParameter("bankCode");
        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        // vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
        String vnp_IpAddr = VnpayConfig.getIpAddress(request);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_OrderType", orderType);

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        Transaction transaction = new Transaction();
        transaction.setTxnRef(vnp_TxnRef);
        transaction.setStatus(0);
        transaction.setTotalPrice(amountres);
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setOrder(consumer);
        transactionRepository.save(transaction);

        List<Cart> carts = cartRepository.findAllById(items);
        for (Cart cart : carts) {
            cart.setTransaction(transaction);

            int basePrice = (cart.getProductVatiants() != null)
                    ? cart.getProductVatiants().getPrice()
                    : cart.getProduct().getPrice();

            int discount = (cart.getProduct().getDiscount() != null)
                    ? cart.getProduct().getDiscount()
                    : 0;

            int totalPrice = cart.getOrderQuantity() * basePrice * (100 - discount) / 100;
            cart.setTotalPrice(totalPrice);

            cart.setModifiedDate(LocalDateTime.now());
            cartRepository.save(cart);
        }

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey,
                hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
        // PaymentDto paymentDto = new PaymentDto();
        // paymentDto.setStatus("Ok");
        // paymentDto.setMessage("success");
        // paymentDto.setURL(paymentUrl);
        // model.addAttribute("payment", paymentDto);
        return "redirect:" + paymentUrl;
    }

    @GetMapping("/payment_infor")
    public String paymentInfor(@RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String blankCode,
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "vnp_TxnRef") String traceId, Model model) {
        PaymentDto paymentDto = new PaymentDto();
        if (responseCode.equals("00")) {
            paymentDto.setStatus("Ok");
            paymentDto.setMessage("success");
            paymentDto.setURL("thanh toán thành công");
            Transaction transaction = transactionRepository.findByTxnRefAndStatusFalse(traceId);
            transaction.setStatus(1);
            transaction.setVnpTransactionNo(blankCode);
            transaction.setVnpOrderInfo(order);
            transaction.setTraceId(traceId);
            transaction.setModifiedDate(LocalDateTime.now());
            transactionRepository.save(transaction);
            List<Cart> carts = cartRepository.findAllByTransactionId(transaction.getId());
            carts.forEach(cart -> {
                if(cart.getProductVatiants() != null) {
                    cart.getProductVatiants().setQuantity(cart.getProductVatiants().getQuantity() - cart.getOrderQuantity());
                }
                cart.getProduct().setQuantity(cart.getProduct().getQuantity() - cart.getOrderQuantity());
                cart.setModifiedDate(LocalDateTime.now());
                cart.setShippingStatus(1); 
                cartRepository.save(cart);
            });

        } else {
            paymentDto.setStatus("Ok");
            paymentDto.setMessage("success");
            paymentDto.setURL("thanh toán không thành công");
            Transaction transaction = transactionRepository.findByTxnRefAndStatusFalse(traceId);
            if (transaction != null) {
                transaction.setVnpTransactionNo(blankCode);
                transaction.setVnpOrderInfo(order);
                transaction.setTraceId(traceId);
                transaction.setModifiedDate(LocalDateTime.now());
                transactionRepository.save(transaction);
                List<Cart> carts = cartRepository.findAllByTransactionId(transaction.getId());
                carts.forEach(cart -> {
                    cart.setTransaction(null);
                    cart.setModifiedDate(LocalDateTime.now());
                    cartRepository.save(cart);
                });
            }
        }
        model.addAttribute("payment", paymentDto);
        return "testVnpay/vnpayres";
    }
}
