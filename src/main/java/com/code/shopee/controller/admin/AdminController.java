package com.code.shopee.controller.admin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.shopee.dto.MailDto;
import com.code.shopee.service.MailService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
	private MailService sendmail;

    @RequestMapping("/index")
    public String Admin() {
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁 Start sending email at: " + startTime);
        MailDto mess = new MailDto("thanhtyu147@gmail.com", "anh nhac em", "coi chung tao do");
        sendmail.sendEmail(mess);
        LocalDateTime endTime = LocalDateTime.now();
        
        System.out.println("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁Email sent at: " + endTime);
        long seconds = ChronoUnit.SECONDS.between(startTime, endTime);
        System.out.println("Time difference in seconds: " + seconds);
        System.out.println("send mail thanh cong");
        return "admin/index";
    }
}
