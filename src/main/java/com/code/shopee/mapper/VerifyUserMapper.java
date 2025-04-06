package com.code.shopee.mapper;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.VerifyUserDto;
import com.code.shopee.model.User;

@Component("spring")
public interface  VerifyUserMapper {
    public User toUser(User user, VerifyUserDto verifyUserDto);
    public VerifyUserDto toVerifyUserDto(User user);
}
