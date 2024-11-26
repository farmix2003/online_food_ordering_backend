package com.farmix.service;

import com.farmix.entity.Category;
import com.farmix.entity.Menu;
import com.farmix.entity.Restaurant;
import com.farmix.request.FoodRequest;

import java.util.List;

public interface MenuService {

    public Menu addFood(FoodRequest req, Category category, Restaurant restaurant);
    public void deleteFood(Long id) throws Exception;
    public Menu getFoodById(Long id) throws Exception;
    public List<Menu> getAllFoods(Long restaurantId, String category) throws Exception;
    public List<Menu> searchFood(String keyword) throws Exception;
    public Menu updateAvailableStatus(Long id) throws Exception;

}
