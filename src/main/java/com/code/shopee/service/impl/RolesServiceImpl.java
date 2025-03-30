package com.code.shopee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.shopee.model.Roles;
import com.code.shopee.repository.RolesRepository;
import com.code.shopee.service.RolesService;

@Service
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesRepository rolesRepository;
    @Override
    public Roles findById(int id) {
        return rolesRepository.findById(id).orElse(null);
    }

    @Override
    public Roles findByRole(String role) {
        return rolesRepository.findByRole(role).orElse(null);
    }
    
}
