package com.farmix.service;

import com.farmix.entity.Category;
import com.farmix.entity.Food;
import com.farmix.entity.Restaurant;
import com.farmix.request.FoodRequest;

import java.util.List;

public interface FoodService {

    public Food addFood(FoodRequest req, Category category, Restaurant restaurant);
    public void deleteFood(Long id) throws Exception;
    public Food getFoodById(Long id) throws Exception;
    public List<Food> getAllFoods(Long restaurantId, String category) throws Exception;
    public List<Food> searchFood(String keyword) throws Exception;
    public Food updateAvailableStatus(Long id) throws Exception;

}
