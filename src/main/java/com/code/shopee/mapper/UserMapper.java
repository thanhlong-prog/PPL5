package com.code.shopee.mapper;

import org.springframework.stereotype.Component;

import com.code.shopee.dto.UserDto;
import com.code.shopee.model.User;

@Component("spring")
public interface UserMapper {
    public UserDto toUserDto(User user);
    public User toUser(UserDto userdto);
    public void updateUser(User user, UserDto userdto);
}
