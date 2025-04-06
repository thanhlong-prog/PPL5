package com.code.shopee.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.shopee.Config.SmsConfig;
import com.code.shopee.api.SpeedSMSAPI;
import com.code.shopee.request.SmsRequest;
import com.code.shopee.service.SmsService;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsConfig smsConfig;
    @Override
    public Boolean sendSms(SmsRequest smsRequest) {
        try {
            if(smsRequest != null) {
                System.out.println("Sending SMS to: " + smsRequest.getReceiver() + ", message: " + smsRequest.getMessage() + ", type: " + smsRequest.getType() + ", sender: " + smsRequest.getSender());
                SpeedSMSAPI API = new SpeedSMSAPI(smsConfig.getAccessToken()); 
                String result = API.sendSMS(smsRequest.getReceiver(), smsRequest.getMessage(), smsRequest.getType(), smsRequest.getSender());
                System.out.println(result);
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> resultMap = objectMapper.readValue(result, Map.class);
                List<String> invalidPhones = (List<String>) resultMap.get("invalidPhone");
                if (invalidPhones != null && invalidPhones.isEmpty() && resultMap.get("status").equals("success")) {
                    return false;
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sending SMS: " + e.getMessage());
        }
    }
}
