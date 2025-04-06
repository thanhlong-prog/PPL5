package com.code.shopee.mapper.impl;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.VerifyUserDto;
import com.code.shopee.mapper.VerifyUserMapper;
import com.code.shopee.model.User;

@Component
public class VerifyUserMapperImpl implements VerifyUserMapper {
    public VerifyUserMapperImpl() {
    }
    @Override
    public User toUser(User user, VerifyUserDto verifyUserDto) {
        if(verifyUserDto == null) {
            return null;
        }
        else {
            user.setName(verifyUserDto.getName());
            user.setGmail(verifyUserDto.getGmail());
            user.setPhone(verifyUserDto.getPhone());
            return user;
        }
    }
    @Override
    public VerifyUserDto toVerifyUserDto(User user) {
        if(user == null) {
            return null;
        }
        else {
            VerifyUserDto verifyUserDto = new VerifyUserDto();
            verifyUserDto.setName(user.getName());
            verifyUserDto.setGmail(user.getGmail());
            verifyUserDto.setPhone(user.getPhone());
            return verifyUserDto;
        }
    }
}
