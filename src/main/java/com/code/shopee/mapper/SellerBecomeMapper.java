package com.code.shopee.mapper;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.SellerBecomeDto;
import com.code.shopee.model.SellerInfo;

@Component("spring")
public interface  SellerBecomeMapper {
    public SellerInfo toSeller(SellerBecomeDto sellerBecomeDto);
}
