package com.code.shopee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.shopee.model.Category;
import com.code.shopee.repository.CategoryRepository;
import com.code.shopee.service.CategoryService;

@Service
public class CategoryServiceImpl  implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategoryStatusTrue() {
        return categoryRepository.findByStatusTrue();
    }
}
