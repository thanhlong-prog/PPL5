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
            user.setPassword(userdto.getPassword());
            user.setBirthday(userdto.getBirthday());
            user.setPhone(userdto.getPhone());
            user.setGmail(userdto.getGmail());
            user.setFacebook(userdto.getFacebook());
            user.setBecomeSellerDate(userdto.getBecomeSellerDate());
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
            user.setPassword(userdto.getPassword());
            user.setBirthday(userdto.getBirthday());
            user.setPhone(userdto.getPhone());
            user.setGmail(userdto.getGmail());
            user.setFacebook(userdto.getFacebook());
            user.setModifiedDate(userdto.getModifiedDate());
        }
    }
}
