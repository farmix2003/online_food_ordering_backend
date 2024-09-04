package com.farmix.service.serviceImpl;

import com.farmix.entity.Category;
import com.farmix.entity.Restaurant;
import com.farmix.repository.CategoryRepository;
import com.farmix.service.CategoryService;
import com.farmix.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public Category createCategory(String categoryName, Long id) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(id);
        Category category = new Category();
        category.setName(categoryName);
        category.setRestaurant(restaurant);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoriesByRestaurantId(Long id) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(id);
        return categoryRepository.findByRestaurantId(restaurant.getId());
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()){
            throw new Exception("Category not found");
        }
        return category.get();
    }
}
