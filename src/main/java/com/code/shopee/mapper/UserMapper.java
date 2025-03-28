package com.code.shopee.mapper;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.UserDto;
import com.code.shopee.model.User;

@Component("spring")
public interface UserMapper {
    User toUser(UserDto userdto);
    void updateUser(User user, UserDto userdto);
}
