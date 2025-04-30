package com.code.shopee.mapper.impl;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.ProfileDto;
import com.code.shopee.mapper.ProfileMapper;
import com.code.shopee.model.User;

@Component
public class ProfileMapperImpl implements ProfileMapper{
    public ProfileMapperImpl() {

    }
    @Override
    public User toUser(ProfileDto profileDto) {
        if(profileDto == null) {
            return null;
        }
        else {
            User user = new User();
            user.setUsername(profileDto.getUsername());
            user.setName(profileDto.getName());
            user.setGmail(profileDto.getGmail());
            user.setPhone(profileDto.getPhone());
            user.setSex(profileDto.getSex());
            user.setBirthday(profileDto.getBirthday());
            user.setAvatar(profileDto.getAvatar());
            return user;
        }
    }
}
