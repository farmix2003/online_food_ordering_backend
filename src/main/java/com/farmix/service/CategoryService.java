package com.farmix.service;

import com.farmix.entity.Category;

import java.util.List;

public interface CategoryService {

    public Category createCategory(String categoryName, Long id) throws Exception;

    public List<Category> findCategoriesByRestaurantId(Long restaurantId) throws Exception;

    public Category findCategoryById(Long id) throws Exception;



}
