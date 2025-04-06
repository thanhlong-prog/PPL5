package com.code.shopee.service;

import com.code.shopee.request.SmsRequest;

public interface  SmsService {
    public Boolean sendSms(SmsRequest smsRequest);
}
