package com.code.shopee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.shopee.Config.SmsConfig;
import com.code.shopee.api.SpeedSMSAPI;
import com.code.shopee.request.SmsRequest;
import com.code.shopee.service.SmsService;



@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsConfig smsConfig;
    @Override
    public void sendSms(SmsRequest smsRequest) {
        try {
            if(smsRequest != null) {
                System.out.println("Sending SMS to: " + smsRequest.getReceiver() + ", message: " + smsRequest.getMessage() + ", type: " + smsRequest.getType() + ", sender: " + smsRequest.getSender());
                SpeedSMSAPI API = new SpeedSMSAPI(smsConfig.getAccessToken()); 
                String result = API.sendSMS(smsRequest.getReceiver(), smsRequest.getMessage(), smsRequest.getType(), smsRequest.getSender());
                System.out.println(result);
            }
            else {
                System.out.println("SmsRequest is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send SMS: " + e.getMessage());
        }
    }
}
