package com.code.shopee.mapper.impl;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.mapper.RegistrationMapper;
import com.code.shopee.model.User;

@Component
public class RegistrationMapperImpl implements RegistrationMapper {
    public RegistrationMapperImpl() {
    }
    @Override
    public User toUser(User user,  RegistrationDto registrationDto) {
        if(registrationDto == null) {
            return null;
        }
        else {
            user.setUsername(registrationDto.getUsername());
            user.setName(registrationDto.getName());
            user.setGmail(registrationDto.getGmail());
            user.setPhone(registrationDto.getPhone());
            user.setAvatar(registrationDto.getAvatar());
            user.setPassword(registrationDto.getPassword());
            user.setCreatedDate(registrationDto.getCreatedDate());
            user.setRoles(registrationDto.getRoles());
            user.setModifiedDate(registrationDto.getModifiedDate());
            user.setVerify(registrationDto.getVerify());
            user.setEnable(registrationDto.getEnable());
            user.setStatus(registrationDto.getStatus());
            return user;
        }
    }

}
