package com.code.shopee.service;

import com.code.shopee.model.User;

public interface UserService {
    User findByUsername(String username);
}
