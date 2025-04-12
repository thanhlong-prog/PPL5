package com.code.shopee.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    public String getImageUrl(MultipartFile file) throws Exception;
}
