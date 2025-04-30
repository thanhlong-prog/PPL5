package com.code.shopee.mapper;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.ProfileDto;
import com.code.shopee.model.User;

@Component("spring")
public interface ProfileMapper {
    public User toUser(ProfileDto profileDto);
}
