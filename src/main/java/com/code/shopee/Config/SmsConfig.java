package com.code.shopee.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "sms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsConfig {
    private String deviceId;
    private String accessToken;
}
