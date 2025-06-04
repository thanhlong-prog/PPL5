package com.code.shopee.service;

import java.util.List;

import com.code.shopee.model.Category;
import com.code.shopee.model.Subcategory;

public interface CategoryService {
    public List<Category> getAllCategoryStatusTrue();
    public Category getCategoryByIdAndStatusTrue(int id);
    public List<Subcategory> getListSubCatesByStatusTrue(int categoryId);
}
