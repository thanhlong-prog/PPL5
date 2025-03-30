package com.code.shopee.service;

import com.code.shopee.model.Roles;

public interface  RolesService {
    public Roles findById(int id);
    public Roles findByRole(String role);
}
