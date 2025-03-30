package com.code.shopee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.mapper.RegistrationMapper;
import com.code.shopee.model.User;
import com.code.shopee.repository.UserRepository;
import com.code.shopee.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegistrationMapper registrationMapper;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void save(RegistrationDto registrationDto) {
        User user = new User();
        registrationMapper.toUser(user, registrationDto);
        userRepository.save(user);
    }
}
