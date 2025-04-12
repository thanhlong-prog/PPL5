package com.code.shopee.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CloudinaryConfig {
    private String url;
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(url);
    }
}   
