package com.code.shopee.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.shopee.dto.ProfileDto;
import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.dto.VerifyUserDto;
import com.code.shopee.mapper.RegistrationMapper;
import com.code.shopee.mapper.VerifyUserMapper;
import com.code.shopee.model.User;
import com.code.shopee.repository.UserRepository;
import com.code.shopee.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegistrationMapper registrationMapper;
    @Autowired
    private VerifyUserMapper verifyUserMapper;

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

    @Override
    public void save(VerifyUserDto verifyUserDto) {
        Optional<User> userOptional = userRepository.findByUsername(verifyUserDto.getUsername());
        User user = userOptional.orElse(null);
        if (user != null) {
            user.setName(verifyUserDto.getName());
            user.setGmail(verifyUserDto.getGmail());
            user.setPhone(verifyUserDto.getPhone());
            user.setModifiedDate(verifyUserDto.getModifiedDate());
            user.setVerify(verifyUserDto.getVerify());
            userRepository.save(user);
        }
    }

    @Override
    public void save(ProfileDto profileDto) {
        Optional<User> userOptional = userRepository.findByUsername(profileDto.getUsername());
        User user = userOptional.orElse(null);
        if (user != null) {
            user.setName(profileDto.getName());
            user.setGmail(profileDto.getGmail());
            user.setPhone(profileDto.getPhone());
            user.setSex(profileDto.getSex());
            user.setBirthday(profileDto.getBirthday());
            user.setAvatar(profileDto.getAvatar());
            user.setModifiedDate(profileDto.getModifiedDate());
            userRepository.save(user);
        }
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByGmail(String gmail) {
        return userRepository.findByGmail(gmail).orElse(null);
    }

    @Override
    public User findByFacebook(String facebook) {
        return userRepository.findByFacebook(facebook).orElse(null);
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }
}
