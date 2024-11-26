package com.farmix.service.serviceImpl;

import com.farmix.dto.CategoryDTO;
import com.farmix.entity.Category;
import com.farmix.entity.Restaurant;
import com.farmix.exception.CartNotFoundException;
import com.farmix.exception.CategoryNotFoundException;
import com.farmix.exception.RestaurantNotFoundException;
import com.farmix.repository.CategoryRepository;
import com.farmix.service.CategoryService;
import com.farmix.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    @Transactional
    public Category createCategory(String categoryName, Long id) throws Exception {

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new CategoryNotFoundException("Category name must not be null or empty.");
        }

        Restaurant restaurant = restaurantService.getRestaurantByUserId(id);

        if (restaurant == null) {
            throw new RestaurantNotFoundException("Restaurant not found for the provided user");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setRestaurant(restaurant);
        return categoryRepository.save(category);
    }

    @Override
    @Cacheable("categories")
    public List<CategoryDTO> findCategoriesByRestaurantId(Long id) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(id);
        return categoryRepository.findByRestaurantId(restaurant.getId())
                .stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Category findCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new CategoryNotFoundException("Category not found");
        }
        return category.get();
    }
}
