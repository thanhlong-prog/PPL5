package com.code.shopee.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.shopee.Config.SmsConfig;
import com.code.shopee.service.MailService;
import com.code.shopee.service.SmsService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
	private MailService sendmail;
    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsConfig smsConfig;

    @RequestMapping("/index")
    public String Admin() {
        // LocalDateTime startTime = LocalDateTime.now();
        // System.out.println("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁 Start sending email at: " + startTime);
        // MailDto mess = new MailDto("thanhtyu147@gmail.com", "anh nhac em", "coi chung tao do");
        // sendmail.sendEmail(mess);
        // LocalDateTime endTime = LocalDateTime.now();
        
        // System.out.println("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁Email sent at: " + endTime);
        // long seconds = ChronoUnit.SECONDS.between(startTime, endTime);
        // System.out.println("Time difference in seconds: " + seconds);
        // System.out.println("send mail thanh cong");

        // LocalDateTime startTime = LocalDateTime.now();
        // System.out.println("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁 Start sending email at: " + startTime);
        // SmsRequest smsRequest = new SmsRequest("0772415808", "hello world", 2, smsConfig.getDeviceId());
        // smsService.sendSms(smsRequest);
        // LocalDateTime endTime = LocalDateTime.now();
        // System.out.println("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁Email sent at: " + endTime);
        // long seconds = ChronoUnit.SECONDS.between(startTime, endTime);
        // System.out.println("Time difference in seconds: " + seconds);
        return "admin/index";
    }
}
