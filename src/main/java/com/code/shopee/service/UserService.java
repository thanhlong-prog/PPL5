package com.code.shopee.service;

import java.util.List;

import com.code.shopee.dto.ProfileDto;
import com.code.shopee.dto.RegistrationDto;
import com.code.shopee.dto.VerifyUserDto;
import com.code.shopee.model.User;
import com.code.shopee.model.UserAddress;


public interface UserService {
    User findByUsername(String username);
    User findById(int id);
    void save(RegistrationDto registrationDto);
    void save(VerifyUserDto verifyUserDto);
    void save(ProfileDto profileDto);
    void save(User user);
    public User findByGmail(String gmail);
    public User findByFacebook(String facebook);
    public User findByPhone(String phone);
    public List<UserAddress> getAllUserAddressByStatusTrue(int userId);
    public UserAddress getUserAddressByStatusTrue(int id);
    public void saveUserAddress(UserAddress userAddress);
    public void deleteUserAddress(int id);
    public UserAddress getUserAddressByIsDefault(int isDefault);
}
