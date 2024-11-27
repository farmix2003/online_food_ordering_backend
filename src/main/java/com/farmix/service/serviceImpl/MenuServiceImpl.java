package com.farmix.service.serviceImpl;

import com.farmix.entity.Category;
import com.farmix.entity.Menu;
import com.farmix.entity.Restaurant;
import com.farmix.exception.FoodNotFoundException;
import com.farmix.repository.FoodRepository;
import com.farmix.request.FoodRequest;
import com.farmix.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    @Transactional
    public Menu addFood(FoodRequest req, Category category, Restaurant restaurant) {

        if (req.getFoodName() == null || req.getFoodName().isEmpty()) {
            throw new IllegalArgumentException("Food name must not be null or empty.");
        }

        Menu food = new Menu();
        food.setCategory(category);
        food.setRestaurant(restaurant);
        food.setFoodName(req.getFoodName());
        food.setDescription(req.getDescription());
        food.setImagesList(req.getImages());
        food.setExtrasList(req.getExtras());
        food.setPrice(req.getPrice());
        Menu savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    @Transactional
    public void deleteFood(Long id) throws Exception {
        Menu food = getFoodById(id);
        food.setRestaurant(null);
        foodRepository.save(food);
    }

    @Override
    public Menu getFoodById(Long id) throws Exception {
      return foodRepository.findById(id)
              .orElseThrow(() -> new FoodNotFoundException("Food not found with id "+id));
    }

    @Override
    @Transactional
    public List<Menu> getAllFoods(Long restaurantId, String category) throws Exception {
        List<Menu> menuList = foodRepository.findByRestaurantId(restaurantId);
        if(category != null && !category.isEmpty()){
            menuList = filterByFoodCategory(menuList, category);
        }
        return menuList;
    }

    private List<Menu> filterByFoodCategory(List<Menu> foods, String category) {
      return foods.stream().filter(food ->
              food.getCategory() != null && food.getCategory().getName().equals(category)
              ).collect(Collectors.toList());
    }

    @Override
    public List<Menu> searchFood(String keyword) throws Exception {
        return foodRepository.searchFood(keyword);
    }

    @Override
    @Transactional
    public Menu updateAvailableStatus(Long id) throws Exception {
        Menu food = getFoodById(id);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
