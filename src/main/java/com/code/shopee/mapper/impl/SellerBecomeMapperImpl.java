package com.code.shopee.mapper.impl;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.SellerBecomeDto;
import com.code.shopee.mapper.SellerBecomeMapper;
import com.code.shopee.model.SellerInfo;

@Component
public class SellerBecomeMapperImpl implements SellerBecomeMapper {
    public SellerBecomeMapperImpl() {
    }
    @Override
    public SellerInfo toSeller(SellerBecomeDto sellerBecomeDto) {
        if (sellerBecomeDto == null) {
            return null;
        }
        else {
            SellerInfo seller = new SellerInfo();
            seller.setShopName(sellerBecomeDto.getShopName());
            seller.setAddress(sellerBecomeDto.getAddress());
            seller.setBusinessType(sellerBecomeDto.getBusinessType());
            seller.setDistrict(sellerBecomeDto.getDistrict());
            seller.setEmail(sellerBecomeDto.getEmail());
            seller.setPhone(sellerBecomeDto.getPhone());
            seller.setEmailReceiveBill(sellerBecomeDto.getEmailReceiveBill());
            seller.setFullAddress(sellerBecomeDto.getFullAddress());
            seller.setIdentificationBack(sellerBecomeDto.getIdentificationBack());
            seller.setIdentificationFront(sellerBecomeDto.getIdentificationFront());
            seller.setIdentificationNumber(sellerBecomeDto.getIdentificationNumber());
            seller.setIdentificationName(sellerBecomeDto.getIdentificationName());
            seller.setTaxCode(sellerBecomeDto.getTaxCode());
            seller.setProvince(sellerBecomeDto.getProvince());
            seller.setFullname(sellerBecomeDto.getFullname());
            seller.setWard(sellerBecomeDto.getWard());
            if(sellerBecomeDto.getCompany() != null) {
                seller.setCompany(sellerBecomeDto.getCompany());
            }
            return seller;
        }
    }
}
