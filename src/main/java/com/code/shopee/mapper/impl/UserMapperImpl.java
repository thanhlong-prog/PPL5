package com.code.shopee.mapper.impl;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.UserDto;
import com.code.shopee.mapper.UserMapper;
import com.code.shopee.model.User;

@Component
public class UserMapperImpl implements UserMapper {
    public UserMapperImpl() {

    }
    @Override
    public User toUser(UserDto userdto) {
        if(userdto == null) {
            return null;
        }
        else {
            User user = new User();
            user.setUsername(userdto.getUsername());
            user.setName(userdto.getName());
            user.setBirthday(userdto.getBirthday());
            user.setPhone(userdto.getPhone());
            user.setGmail(userdto.getGmail());
            user.setFacebook(userdto.getFacebook());
            user.setBecomeSellerDate(userdto.getBecomeSellerDate());
            user.setVerify(userdto.getVerify());
            user.setEnable(userdto.getEnable());
            user.setStatus(userdto.getStatus());
            user.setCreatedDate(userdto.getCreatedDate());
            user.setModifiedDate(userdto.getModifiedDate());
            user.setRoles(userdto.getRoles());
            return user;
        }
    }

    @Override
    public void updateUser(User user, UserDto userdto) {
        if(userdto == null) {
            return;
        }
        else {
            user.setBirthday(userdto.getBirthday());
            user.setPhone(userdto.getPhone());
            user.setGmail(userdto.getGmail());
            user.setFacebook(userdto.getFacebook());
            user.setModifiedDate(userdto.getModifiedDate());
        }
    }
    @Override
    public UserDto toUserDto(User user) {
        if(user == null) {
            return null;
        }
        else {
            UserDto userdto = new UserDto();
            userdto.setUsername(user.getUsername());
            userdto.setName(user.getName());
            userdto.setBirthday(user.getBirthday());
            userdto.setPhone(user.getPhone());
            userdto.setGmail(user.getGmail());
            userdto.setFacebook(user.getFacebook());
            userdto.setBecomeSellerDate(user.getBecomeSellerDate());
            userdto.setVerify(user.getVerify());
            userdto.setEnable(user.getEnable());
            userdto.setStatus(user.getStatus());
            userdto.setCreatedDate(user.getCreatedDate());
            userdto.setModifiedDate(user.getModifiedDate());
            userdto.setRoles(user.getRoles());
            return userdto;
        }
    }
}
