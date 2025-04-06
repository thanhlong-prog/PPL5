package com.code.shopee.mapper;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.model.User;

@Component("spring")
public interface  RegistrationMapper {
    public User toUser(User user ,RegistrationDto registrationDto);
}
