package com.code.shopee.service;

import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.model.User;


public interface UserService {
    User findByUsername(String username);
    User findById(int id);
    void save(RegistrationDto registrationDto);
    void save(User user);
    public User findByGmail(String gmail);
}
